package com.ElectricityAutomationInitiative.payload;


import lombok.Data;


@Data
public class PaymentRequestDTO {
    private String customerId;
    private String tokenId;
    private boolean paid;
}

