package com.ElectricityAutomationInitiative.service;


import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;

import java.util.List;

public interface BillService {
   public void generateBillForCustomersOn20th();


    // Add your business logic to calculate the bill amount for the customer based on billing plan, usage, etc.
    double calculateBillAmountForCustomer(Connection connection);

    Object getBillDetailsByCustomerId(String customerId, boolean paid);
   // Object getBillDetailsByCustomerId(String customerId, boolean paid);


 public BillDetailsDTO saveBill(BillDetailsDTO billDetailsDTO);

    List<BillDetailsDTO> findPaidBillsByTehsil(String tehsil);


    List<BillDetailsDTO> findAllPaidBillsByTehsil(String tehsil);

    List<BillDetailsDTO> findAllPaidBillsByCustomerId(String customerId);


    List<BillDetailsDTO> getPendingBills();

    List<BillDetailsDTO> getPaidBills();

    List<BillDetailsDTO> findAllUnPaidBillsByCustomerId(String customerId);

}

