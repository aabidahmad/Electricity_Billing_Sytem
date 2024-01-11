package com.ElectricityAutomationInitiative.payload;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillDetailsDTO {
    // Customer details from Connection entity
    private String customerName;
    private String customerId;
    private String address;
    private String postalCode;
    private String tehsil;
    private String district;
    private String email;

    private LocalDate billingDate;
    private LocalDate dueDate;
    private double totalAmount;
    private double amountAfterDueDate;
    private double paidAmount;
    private boolean paid;
    private LocalDateTime billPaidDate;


}

