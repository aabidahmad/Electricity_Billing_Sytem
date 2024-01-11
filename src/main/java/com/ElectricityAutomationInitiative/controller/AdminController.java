package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.entity.*;
import com.ElectricityAutomationInitiative.payload.AdminDetails;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.payload.ConnectionDTO;
import com.ElectricityAutomationInitiative.payload.ConnectionResponse;
import com.ElectricityAutomationInitiative.repository.BillRepository;
import com.ElectricityAutomationInitiative.repository.ComplaintRepository;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.service.*;
import com.ElectricityAutomationInitiative.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin/api")
public class AdminController {
    @Autowired
    ConnectionService connectionService;
    @Autowired
    AdminService adminService;
    @Autowired
    DocumentService documentService;
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    BillService billService;
    @Autowired
    BillRepository billRepository;
    @Autowired
    ConnectionRepository connectionRepository;
    @Autowired
    ComplaintService complaintService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    NotificationService notificationService;
    @Autowired
    ComplaintRepository complaintRepository;
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pending-connections")
    public List<ConnectionResponse> getPendingConnections(@RequestParam("workplace") String workplace) {
        return adminService.getPendingConnectionDTOs(workplace);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getDocument")
    public byte[] getDocuments(@RequestParam("customerId") String customerId) {
        System.out.println("Document Api ");
        return documentService.getDocumentsByCustomerId(customerId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getDetils")
    public AdminDetails fetchDetails(@RequestParam("employeeId") String employeeId) {
        System.out.println(employeeId);
        AdminDetails adminDetails = adminService.fetchDetails(employeeId);
        return adminDetails;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve-connection")
    public ResponseEntity<String> ActivateConnection(@RequestBody Map<String, String> requestParams) {
        String customerId = requestParams.get("customerId");
        String email = requestParams.get("email");
        String username = requestParams.get("username");

        if (adminService.activateConnection(customerId)) {
            emailUtil.sendConnectionApprovedEmail(email, username, customerId);
            return ResponseEntity.ok("Connection approved Successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to activate connection");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rejectConnection")
    public ResponseEntity<String> activateConnection(@RequestBody Map<String, String> requestParams) {
        String customerId = requestParams.get("customerId");
        String email = requestParams.get("email");
        String username = requestParams.get("username");

        if (adminService.rejectConnection(customerId)) {
            emailUtil.sendConnectionRejectionEmail(email, username, customerId);
            return ResponseEntity.ok("Connection approved Successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to activate connection");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/paid-bills")
    public List<BillDetailsDTO> findAllPaidBillsByTehsil(@RequestParam("tehsil") String tehsil) {

        List<BillDetailsDTO> paidBillsByTehsil = billService.findPaidBillsByTehsil(tehsil);
        return paidBillsByTehsil;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-paid-bills")
    public List<BillDetailsDTO> findCurrentMonethsPaidBills(@RequestParam("tehsil") String tehsil) {

        List<BillDetailsDTO> paidBillsByTehsil = billService.findAllPaidBillsByTehsil(tehsil);
        return paidBillsByTehsil;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> updateAdminDetails(
            @RequestParam("employeeId") String employeeId,
            @RequestBody AdminDetails updatedAdmin
    ) {
        try {
            AdminDetails adminDetails = adminService.updateAdminDetails(employeeId, updatedAdmin);
            if (adminDetails != null) {
                return ResponseEntity.ok("Your Details Are Update Successfully ");
            } else {
                String message = "Used the Number of allowed Updates ";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Server Error Occured In updating details ");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-tehsil")
    public ResponseEntity<List<Complaint>> getComplaintsByTehsil(@RequestParam("tehsil") String tehsil) {
        System.out.println("getComplaints "+tehsil);
        List<Complaint> complaints = complaintService.getAllComplaints(tehsil);
        return ResponseEntity.ok(complaints);
    }
    private String resetOtp;
    Admin admin;
    @PostMapping("/initiate")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam("emailOrId") String emailOrId) {
         admin = adminService.getByEmailOrId(emailOrId);
        if (admin != null) {
            resetOtp= emailUtil.sendPasswordResetEmail(admin.getEmail());
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
        if (admin != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            boolean b = adminService.resetPassword(admin.getEmployeeId(), encodedPassword);
            if (b) {
                return ResponseEntity.ok().body("Password Reset Successfully");
            }
        }
        return ResponseEntity.badRequest().body("Error in Resetting password");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notification")
    public ResponseEntity<?> postNotification(@RequestBody Notification notification) {
        // You can add validation logic here if needed
        Notification notification1 = notificationService.saveNotification(notification);
        if(notification1 != null){
            return ResponseEntity.ok().body("Notification is Posted Successfully ");
        }
        else{
            return ResponseEntity.badRequest().body("Error in Posting Notification");
        }
    }
   // @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/connections/byDistrict")
    public Map<String, Integer> getConnectionsByDistrict() {
        List<Connection> connections = connectionRepository.findAll();
        Map<String, Integer> connectionData = new HashMap<>();

        for (Connection connection : connections) {
            String district = connection.getDistrict();
            int totalConnections = 1; // Assuming each entry represents one connection, adjust this logic if needed

            // If the key already exists, add the total connections to the existing value
            connectionData.put(district, connectionData.getOrDefault(district, 0) + totalConnections);
        }
        return connectionData;
    }
    @RequestMapping("/active/connections")
    public Map<String, Integer> getActiveConnectionsByDistrict() {
        List<Connection> connections = connectionRepository.findByStatus(ConnectionStatus.ACTIVE);
        Map<String, Integer> connectionData = new HashMap<>();
        for (Connection connection : connections) {
            String district = connection.getDistrict();
            int totalConnections = 1; // Assuming each entry represents one connection, adjust this logic if needed
            // If the key already exists, add the total connections to the existing value
            connectionData.put(district, connectionData.getOrDefault(district, 0) + totalConnections);
        }
        return connectionData;
    }
    @GetMapping("/pending/connections")
    public ResponseEntity<List<ConnectionDTO>> getAllPendingConnections() {
        List<ConnectionDTO> allPendingConnection = connectionService.getAllPendingConnection();
        return new ResponseEntity<>(allPendingConnection, HttpStatus.OK);
    }
    @GetMapping("/active")
    public ResponseEntity<List<ConnectionDTO>> getAllActiveConnections() {
        List<ConnectionDTO> allPendingConnection = connectionService.getAllActiveConnection();
        return new ResponseEntity<>(allPendingConnection, HttpStatus.OK);
    }
    @GetMapping("/revenue/district")
    public Map<String, Double> getTotalPaidAmountByDistrict() {
        List<Object[]> results = billRepository.calculateTotalPaidAmountByDistrict();
        Map<String, Double> revenueData = new HashMap<>();
        for (Object[] result : results) {
            String district = (String) result[0];
            Double totalPaidAmount = (Double) result[1];
            revenueData.put(district, totalPaidAmount);
        }
        return revenueData;
    }
    @GetMapping("/revenue/year")
    public ResponseEntity<Map<String, Double>> getTotalPaidAmountByMonthYear() {
        List<Object[]> results = billRepository.calculateTotalPaidAmountByYearAndMonth();
        Map<String, Double> monthlyRevenueMap = new HashMap<>();
        for (Object[] result : results) {
            int year = (int) result[0];
            int month = (int) result[1];
            double totalPaidAmount = (double) result[2];

            String monthYear = String.format("%02d-%04d", month, year);
            monthlyRevenueMap.put(monthYear, totalPaidAmount);
        }
        monthlyRevenueMap.forEach((monthYear, totalPaidAmount) -> {
            System.out.println(monthYear + " : " + totalPaidAmount);
        });

        return new ResponseEntity<>(monthlyRevenueMap, HttpStatus.OK);
    }
    @GetMapping("/pending/bills")
    public List<BillDetailsDTO> getAllPendingBills() {
        List<BillDetailsDTO> pendingBills = billService.getPendingBills();
        return pendingBills;
    }
    @GetMapping("/paid/bills")
    public List<BillDetailsDTO> getAllPaidBills() {
        List<BillDetailsDTO> pendingBills = billService.getPaidBills();
        return pendingBills;
    }
    @GetMapping("/unpaidrevenue/district")
    public ResponseEntity<Map<String, Double>> getUnpaidRevenueByTehsil() {
        List<Object[]> results = billRepository.calculateUnpaidRevenueByDistrict();
        Map<String, Double> unpaidRevenueByTehsil = new HashMap<>();
        for (Object[] result : results) {
            String tehsil = (String) result[0];
            Double totalAmount = (Double) result[1];
            unpaidRevenueByTehsil.put(tehsil, totalAmount);
        }
        return ResponseEntity.ok(unpaidRevenueByTehsil);
    }
    @GetMapping("/unPaidRevenue/year")
    public ResponseEntity<List<Map<String, Object>>> getUnpaidRevenueGroupedByMonthAndYear() {
        List<Map<String, Object>> unpaidRevenueData = billRepository.findUnpaidRevenueGroupedByMonthAndYear();
        if (unpaidRevenueData != null && !unpaidRevenueData.isEmpty()) {
            return ResponseEntity.ok(unpaidRevenueData);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/district-bill-count")
    public ResponseEntity<List<Object[]>> getBillCountByDistrict() {
        List<Object[]> billCounts = billRepository.getBillCountByDistrict();
        return ResponseEntity.ok(billCounts);
    }
    @GetMapping("/bill-count-by-month")
    public ResponseEntity<List<Map<String, Object>>> getBillCountByMonth() {
        List<Object[]> billCounts = billRepository.getBillCountByMonth();
        List<Map<String, Object>> formattedBillCounts = new ArrayList<>();
        for (Object[] result : billCounts) {
            int month = (int) result[0];
            int year = (int) result[1];
            long billCount = (long) result[2];
            String formattedDate = String.format("%02d/%04d", month, year);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("date", formattedDate);
            resultMap.put("billCount", billCount);
            formattedBillCounts.add(resultMap);
        }
        return ResponseEntity.ok(formattedBillCounts);
    }
    @GetMapping("/current-or-previous-months-paid")
    public ResponseEntity<List<Bill>> getCurrentOrPreviousMonthsPaidBills() {
        List<Bill> paidBills = billRepository.findCurrentOrPreviousMonthsPaidBills();
        return ResponseEntity.ok(paidBills);
    }
    @GetMapping("/general/details")
    public Map<String, Object> getTotalActiveConnections() {
        int totalActiveConnections = connectionRepository.countActiveConnections();
        Double totalPaidAmount = billRepository.getTotalPaidAmount();
        int paiBills = billRepository.countAllPaidBills();
        long complaints = complaintRepository.count();
        Map<String, Object> response = new HashMap<>();
        response.put("totalActiveConnections", totalActiveConnections);
        response.put("totalRevenue", totalPaidAmount);
        response.put("paiBills",paiBills);
        response.put("complaints",complaints);
        return response;
    }
}
