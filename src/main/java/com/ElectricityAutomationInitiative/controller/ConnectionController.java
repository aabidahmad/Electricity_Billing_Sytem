package com.ElectricityAutomationInitiative.controller;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.Notification;
import com.ElectricityAutomationInitiative.entity.StationDetails;
import com.ElectricityAutomationInitiative.payload.*;
import com.ElectricityAutomationInitiative.repository.BillRepository;
import com.ElectricityAutomationInitiative.service.*;
import com.ElectricityAutomationInitiative.util.CustomIdGenerator;
import com.ElectricityAutomationInitiative.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class ConnectionController {

    private final ConnectionService connectionService;


    private final CustomIdGenerator customIdGenerator;


    private final EmailUtil emailUtil;

    private final PasswordEncoder passwordEncoder;

      private ConnectionDTO connectionDTO1=null;
    private String otp="";


    ComplaintService complaintService;
    private final StationDetailsService stationDetailsService;
    @Autowired
    BillService billService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    BillRepository billRepository;

    public ConnectionController(ConnectionService connectionService, EmailUtil emailUtil,
                                PasswordEncoder passwordEncoder,
                                ComplaintService complaintService,
                                StationDetailsService stationDetailsService,
                                CustomIdGenerator customIdGenerator) {
        this.connectionService = connectionService;
        this.emailUtil = emailUtil;
        this.customIdGenerator = customIdGenerator;
        this.passwordEncoder = passwordEncoder;

        this.complaintService=complaintService;
        this.stationDetailsService=stationDetailsService;

    }
    @PostMapping("/connections")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody RegResponse response) {

        Map<String, String> responseBody = new HashMap<>();

        if (!connectionService.isEmailUnique(response.getEmail())) {
            responseBody.put("message", "Email Already Exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }

        // Check if phone number is unique
        if (!connectionService.isPhoneNumberUnique(response.getPhoneNumber())) {
            responseBody.put("message", "Phone Number Already Exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }

        // Send OTP to email
        otp = emailUtil.sendOTP(response.getEmail());
        System.out.println("OTP is "+otp);
        responseBody.put("message", "OTP sent to email");
       // responseBody.put("customerId",customIdGenerator.generateUniqueId());

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyReg(@Valid @RequestBody ConnectionDTO connectionDTO,HttpSession session) {
        Map<String, String> response = new HashMap<>();

        // Verify the OTP received from the user
        System.out.println("Data " + connectionDTO);

        if (connectionDTO == null || otp == null) {
            response.put("error", "Connection data not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!otp.equals(connectionDTO.getOtp())) {
            response.put("error", "Invalid OTP");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        connectionDTO.setCustomerId(customIdGenerator.generateUniqueId());
        String encodedPassword = passwordEncoder.encode(connectionDTO.getPassword());
        connectionDTO.setPassword(encodedPassword);

        // OTP is valid, save the connection
        Connection savedConnection = connectionService.registerConnection(connectionDTO);
      emailUtil.sendRegistrationEmail(connectionDTO.getEmail(),connectionDTO.getFullName(),connectionDTO.getCustomerId());
      //  session.setAttribute("customerId", connectionDTO.getCustomerId());
       // response.put("success", "Connection registered successfully");
        System.out.println("Data " + connectionDTO);
        response.put("customerId", savedConnection.getCustomerId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get-customer-id")
    public ResponseEntity<String> getCustomerId(HttpServletRequest request) {
        String customerId = (String) request.getSession().getAttribute("customerId");
        System.out.println("CustomerId is "+customerId);
        if (customerId != null) {
            return ResponseEntity.ok(customerId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user-details")
    public ResponseEntity<ConnectionDetails> getUserDetailsByJson(@RequestBody Map<String, String> request) {
        String customerId = request.get("customerId");

        // Replace this with your logic to fetch user details by customerId from a database
        ConnectionDetails connectionDetails= connectionService.getConnectionDetailsByCustomerId(customerId);

        if (connectionDetails != null) {
            return new ResponseEntity<>(connectionDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/view")
    public ResponseEntity<StationDetails> getStationDetails(@RequestParam("workplace") String workplace) {
        System.out.println(workplace);
        StationDetails stationDetails = stationDetailsService.getStationDetailsById(workplace);
        return ResponseEntity.ok(stationDetails);
    }
    @PostMapping("/log-complaint")
    public ResponseEntity<String> logComplaint(@RequestBody ComplaintDTO complaint) {
        System.out.println(complaint.getCustomerId());
        complaintService.logComplaint(complaint.getCustomerId(), complaint.getDescription());
        return ResponseEntity.ok("Complaint logged successfully");

    }
        private String resetOtp;
       private Connection connection;
        @PostMapping("/initiate")
        public ResponseEntity<String> initiatePasswordReset(@RequestParam("emailOrId") String emailOrId) {
            connection = connectionService.getByEmailOrCustomerId(emailOrId,emailOrId);
            if (connection != null) {
                resetOtp= emailUtil.sendPasswordResetEmail(connection.getEmail());
                return ResponseEntity.ok().body("OTP is sent on email");
            } else {
                return ResponseEntity.badRequest().body("Email is not Valid ");
            }
        }
    @PostMapping("/verify")
    public ResponseEntity<String> confirmReset(@RequestParam("otp") String otp) {
        // Validate the reset token and check if it's still valid
        if (resetOtp.equals(otp)) {
            // If valid, allow the user to proceed with resetting the password
            return ResponseEntity.ok("Reset token is valid. Please reset your password.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired reset token.");
        }
    }
            @PostMapping("/change")
            public ResponseEntity<String>verify(@RequestParam("newPassword") String newPassword) {
                if (connection != null) {
                    String encodedPassword = passwordEncoder.encode(newPassword);
                    boolean b = connectionService.resetPassword(connection.getCustomerId(), encodedPassword);
                    if (b) {
                        return ResponseEntity.ok().body("Password Reset Successfully");
                    }
                }
                return ResponseEntity.badRequest().body("Error in Resetting password");
            }
    @GetMapping("/all-paid-bills")
    public List<BillDetailsDTO> getAllPaidBills(@RequestParam("customerId") String customerId) {
       // System.out.println("is sisis "+customerId);
        List<BillDetailsDTO> paidBillsByTehsil = billService.findAllPaidBillsByCustomerId(customerId);
        System.out.println(paidBillsByTehsil);
        return paidBillsByTehsil;
    }//
    @GetMapping("/all-unpaid-bills")
    public List<BillDetailsDTO> getAllUnPaidBills(@RequestParam("customerId") String customerId) {
        System.out.println("is sisis "+customerId);
        List<BillDetailsDTO> paidBillsByTehsil = billService.findAllUnPaidBillsByCustomerId(customerId);
        System.out.println(paidBillsByTehsil);
        return paidBillsByTehsil;
    }
    @GetMapping("/area")
    public ResponseEntity<List<Notification>> getNotificationsByArea(@RequestParam("area") String area) {
        System.out.println("Your area is "+area);
        List<Notification> notifications = connectionService.getNotifications(area);
        return ResponseEntity.ok(notifications);
    }
    @PostMapping("/update/userDetails")
    public ResponseEntity<String> updateAdminDetails(
            @RequestParam("customerId") String customerId,
            @RequestParam("name") String name,@RequestParam("fathersName") String fathersName
    ) {
            String s = connectionService.updateDetails(customerId,name,fathersName);
            return ResponseEntity.ok(s);
    }
    @PostMapping("/contact/userDetails")
    public ResponseEntity<String> updateContactDetails(
            @RequestParam("customerId") String customerId,
            @RequestParam("phoneNumber") String phoneNumber,@RequestParam("email") String email
    ) {
        String s = connectionService.updateContactDetails(customerId,phoneNumber,email);
        return ResponseEntity.ok(s);
    }
    @PostMapping("/updatePassword")
    public ResponseEntity<String>updatePassword( @RequestParam("customerId") String customerId,
                                                 @RequestParam("password") String password,
                                                  @RequestParam("newPassword") String newPassword){
        String encodedPassword = passwordEncoder.encode(password);
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        int i = connectionService.updatePasswordDetails(customerId, encodedPassword, encodedNewPassword);
        if(i!=0){
            return ResponseEntity.ok().body("password Updated Successfully ");
        }
        else{
            return ResponseEntity.badRequest().body(" pass ");
        }
    }
    @GetMapping("/general/details")
    public Map<String, Object> getTotalActiveConnections(@RequestParam("customerId") String customerId) {

        Double totalPaidAmount = billRepository.getTotalPaidAmountByCustomerId(customerId);
        long paidBills = billRepository.countUnPaidBillsByCustomerId(customerId);
        long unPaidBills = billRepository.countUnPaidBillsByCustomerId(customerId);
        Map<String, Object> response = new HashMap<>();
        response.put("totalAmount", totalPaidAmount);
        response.put("paidBills", paidBills);
        response.put("unPaidBills",unPaidBills);
        return response;
    }
}

