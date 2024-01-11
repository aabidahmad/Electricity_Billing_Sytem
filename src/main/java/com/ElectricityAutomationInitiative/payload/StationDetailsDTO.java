package com.ElectricityAutomationInitiative.payload;

import lombok.Data;

@Data
public class StationDetailsDTO {
    private String customerSupportEmail;
    private String customerSupportNumber;
    private double monthlyBillAmount;
    private String stationArea;
    private double penaltyAmount;
}
