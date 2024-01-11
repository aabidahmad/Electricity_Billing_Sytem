package com.ElectricityAutomationInitiative.payload;

import lombok.Data;

@Data
public class AdminDetails {
    private String fullName;
    private String employeeId;
    private String contactNumber;
    private String email;
    private String address;
    private String designation;
    private String workplace;
}
