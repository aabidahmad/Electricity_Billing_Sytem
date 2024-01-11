package com.ElectricityAutomationInitiative.payload;

import lombok.Data;

@Data
public class ConnectionResponse {
    private String customerId;
    private String fullName;
    private String address;
    private String tehsil;
    private String district;
    private String state;
    private String postalCode;
    private byte[] pdfContent;

}
