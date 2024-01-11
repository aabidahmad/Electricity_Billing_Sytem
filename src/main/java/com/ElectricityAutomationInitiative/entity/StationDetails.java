package com.ElectricityAutomationInitiative.entity;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class StationDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String customerSupportEmail;
    private String customerSupportNumber;
    private double monthlyBillAmount;
    private String stationArea;
    private double penaltyAmount;



}
