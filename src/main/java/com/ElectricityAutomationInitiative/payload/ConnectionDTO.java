package com.ElectricityAutomationInitiative.payload;
import com.ElectricityAutomationInitiative.entity.ConnectionStatus;
import lombok.Data;


@Data
public class ConnectionDTO {
    private String customerId;
    private String fullName;
    private String fathersName;
    private String phoneNumber;
    private String email;
    private String address;
    private String tehsil;
    private String district;
    private String city;
    private String state;
    private String postalCode;
    private String password;
    private String otp;
    private ConnectionStatus status;
}