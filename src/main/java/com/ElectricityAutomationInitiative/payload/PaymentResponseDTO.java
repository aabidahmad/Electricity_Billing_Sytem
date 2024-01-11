package com.ElectricityAutomationInitiative.payload;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponseDTO {
    private String status;
    private String message;
    private String customerId;
    private double amountPaid;
    private LocalDateTime paymentDate;
    private String transactionId;
}

