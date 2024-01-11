package com.ElectricityAutomationInitiative.payload;

import lombok.Data;



@Data
public class AdminRegistrationDTO {
    private String username;
    private String fullName;
    private String employeeId;
    private String department;
    private String contactNumber;
    private String email;
    private String address;
    private String dateOfBirth;
    private String designation;
    private String workplace;
    private String password;
    private String otp;
}