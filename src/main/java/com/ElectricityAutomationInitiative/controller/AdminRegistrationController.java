package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.payload.AdminRegistrationDTO;
import com.ElectricityAutomationInitiative.payload.RegResponse;
import com.ElectricityAutomationInitiative.service.AdminRegistrationService;
import com.ElectricityAutomationInitiative.util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping("/admin/api")
public class AdminRegistrationController {

    private final AdminRegistrationService adminRegistrationService;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;
    private String otp = "";

    @Autowired
    public AdminRegistrationController(AdminRegistrationService adminRegistrationService,
                                       EmailUtil emailUtil,
                                       PasswordEncoder passwordEncoder) {
        this.adminRegistrationService = adminRegistrationService;
        this.emailUtil = emailUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveAdmin(@RequestBody RegResponse reg) throws Exception {

        if (!adminRegistrationService.isEmailUnique(reg.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Already Used");
        }

        otp = emailUtil.sendOTP(reg.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP is sent on Email");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/otp")
    public ResponseEntity<?> verifyOtp(@RequestBody AdminRegistrationDTO adminDTO) throws Exception {
        // Check the OTP and perform verification logic here
        System.out.println("Code Executed ");
        Map<String, String> response = new HashMap<>();

        // Verify the OTP received from the use

        if (adminDTO == null || otp == null) {
            response.put("error", "Connection data not found");
            System.out.println("Code Executed ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if (!otp.equals(adminDTO.getOtp())) {
            response.put("error", "Invalid OTP");
            System.out.println("Code Executed ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        // If verification is successful, return a success response
        // Otherwise, return an error response
        String encodedPassword = passwordEncoder.encode(adminDTO.getPassword());
        adminDTO.setPassword(encodedPassword);
        Object result = adminRegistrationService.registerAdmin(adminDTO);
        if (result instanceof String) {
            // If the result is a String, return a bad request response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        } else if (result instanceof Boolean) {
            // If the result is a Boolean, check its value
            Boolean boolResult = (Boolean) result;
            if (boolResult) {
                // If the value is true, return a success response
                response.put("message", "Employee/Admin Successfully Registered ");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                // If the value is false, return some other response
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Already Registered ");
            }
        } else {
            // Handle other types as needed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}


