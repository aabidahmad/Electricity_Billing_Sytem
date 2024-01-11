package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.payload.PaymentRequestDTO;
import com.ElectricityAutomationInitiative.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;

    }

    @PostMapping("/generate")
    public ResponseEntity<Object> getBillDetailsByCustomerId(@RequestBody PaymentRequestDTO paymentRequest) {
        System.out.println("Customer Id "+paymentRequest.getCustomerId());
        Object result = billService.getBillDetailsByCustomerId(paymentRequest.getCustomerId(),paymentRequest.isPaid());
        System.out.println("Customer Id "+result);
        if (result instanceof BillDetailsDTO) {
            BillDetailsDTO billDetailsDTO = (BillDetailsDTO) result;

            return ResponseEntity.ok(billDetailsDTO);
        } else if (result instanceof String) {
            String message = (String) result;
            return ResponseEntity.ok(Collections.singletonMap("message", message));
        } else {
            // Unexpected result, handle the error accordingly
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
