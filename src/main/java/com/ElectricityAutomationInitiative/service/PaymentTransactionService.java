package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.PaymentTransaction;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.payload.PayRequestDTO;

public interface PaymentTransactionService {


    PaymentTransaction processPayment(BillDetailsDTO billDetailsDTO, String tokenId);



    PaymentTransaction processPayment1(BillDetailsDTO billDetailsDTO, String tokenId);
}
