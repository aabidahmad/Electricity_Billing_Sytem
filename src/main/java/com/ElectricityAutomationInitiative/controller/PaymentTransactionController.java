package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.entity.PaymentTransaction;
import com.ElectricityAutomationInitiative.excpetion.PaymentProcessingException;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.payload.PayRequestDTO;
import com.ElectricityAutomationInitiative.payload.PaymentRequestDTO;
import com.ElectricityAutomationInitiative.payload.PaymentResponseDTO;
import com.ElectricityAutomationInitiative.service.BillService;
import com.ElectricityAutomationInitiative.service.PaymentTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/payments")
public class PaymentTransactionController {
    private final PaymentTransactionService paymentTransactionService;
    private final BillService billService;

    @Autowired
    public PaymentTransactionController(PaymentTransactionService paymentTransactionService, BillService billService) {
        this.paymentTransactionService = paymentTransactionService;
        this.billService = billService;
    }

    // Endpoint to initiate the payment for a specific customer
    @PostMapping("/process-payment")
    public ResponseEntity<Object> checkout(@RequestBody PaymentRequestDTO paymentRequest) {

        Object result = billService.getBillDetailsByCustomerId(paymentRequest.getCustomerId(), false);
        if (result instanceof BillDetailsDTO) {

            BillDetailsDTO billDetailsDTO = (BillDetailsDTO) result;
            System.out.println("bill details in paymnt" + billDetailsDTO);
            // Process the payment using the PaymentTransactionService
            PaymentTransaction paymentTransaction = paymentTransactionService.processPayment(billDetailsDTO, paymentRequest.getTokenId());
            // Update the bill status and paid amount in the database
            double totalAmount = billDetailsDTO.getTotalAmount();
            billDetailsDTO.setPaid(true);
            billDetailsDTO.setPaidAmount(totalAmount);
            billDetailsDTO.setTotalAmount(0.0);
            billService.saveBill(billDetailsDTO);
            return ResponseEntity.ok(billService.getBillDetailsByCustomerId(paymentRequest.getCustomerId(), false));
        }
        else{
            return (ResponseEntity<Object>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
//        System.out.println("Entered ");
//        if (result instanceof BillDetailsDTO) {
//
//            BillDetailsDTO billDetailsDTO = (BillDetailsDTO) result;
//            System.out.println("bill details in paymnt"+billDetailsDTO);
//            // Process the payment using the PaymentTransactionService
//            PaymentTransaction paymentTransaction = paymentTransactionService.processPayment(billDetailsDTO,paymentRequest.getTokenId());
//
//
//
//
//            // Prepare the response DTO
//            PaymentResponseDTO responseDTO = new PaymentResponseDTO();
//            responseDTO.setStatus("success");
//            responseDTO.setMessage("Payment successful!");
//            responseDTO.setCustomerId(paymentTransaction.getCustomerId());
//            responseDTO.setAmountPaid(paymentTransaction.getAmountPaid());
//            responseDTO.setPaymentDate(paymentTransaction.getPaymentDate());
//            responseDTO.setTransactionId(paymentTransaction.getChargeId());
//            return ResponseEntity.ok(responseDTO);
//        } else if (result instanceof String || result==null) {
//            {
//                // The bill is already paid, return a string message
//                String message = " Bill Already paid  !";
//                return ResponseEntity.ok(Collections.singletonMap("message", message));
//            }
//        } else  {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
        }
    }

