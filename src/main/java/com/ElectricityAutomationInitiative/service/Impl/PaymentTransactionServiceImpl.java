package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.PaymentTransaction;
import com.ElectricityAutomationInitiative.excpetion.PaymentProcessingException;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.repository.PaymentTransactionRepository;
import com.ElectricityAutomationInitiative.service.PaymentTransactionService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    // Replace these variables with your actual Stripe API keys
    private static final String STRIPE_SECRET_KEY = "sk_test_51NWsjZFDUEjHDSKag8e1c8stPBUzpK1k5qApKrFqtl6kZmm2oG374JTmuJa6F6YXahxP29fDrro8WzQaKcTjWReo000stu7zy6";
    private static final String STRIPE_PUBLIC_KEY = "sk_test_51NWsjZFDUEjHDSKag8e1c8stPBUzpK1k5qApKrFqtl6kZmm2oG374JTmuJa6F6YXahxP29fDrro8WzQaKcTjWReo000stu7zy6";

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public PaymentTransaction processPayment(BillDetailsDTO billDetailsDTO, String tokenId) {
        Stripe.apiKey = STRIPE_SECRET_KEY;


        try {
            // Create PaymentIntent with the Stripe API
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (billDetailsDTO.getTotalAmount() * 100)) // Stripe requires the amount in cents
                    .setCurrency("inr")// Assuming you have the customer ID stored in BillDetailsDTO
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Assuming the PaymentTransaction entity has fields like customerId, amountPaid, paymentDate, and chargeId
            PaymentTransaction paymentTransaction = new PaymentTransaction();
            paymentTransaction.setCustomerId(billDetailsDTO.getCustomerId());
            paymentTransaction.setAmountPaid(billDetailsDTO.getTotalAmount());
            paymentTransaction.setPaymentDate( LocalDateTime.now()); // You can implement the getCurrentDate() method in a utility class
            paymentTransaction.setChargeId(paymentIntent.getId());

            paymentTransactionRepository.save(paymentTransaction);
            billDetailsDTO.setBillPaidDate(LocalDateTime.now());
            return paymentTransaction;
        } catch (StripeException e) {
            throw new PaymentProcessingException("An error occurred during payment processing.", e);
        }
        }






    @Override
    public PaymentTransaction processPayment1(BillDetailsDTO billDetailsDTO, String tokenId) {
        Stripe.apiKey = STRIPE_SECRET_KEY;

        try {
            // Create a Payment Intent
            PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                    .setAmount((long) (billDetailsDTO.getTotalAmount() * 100)) // Convert to cents
                    .setCurrency("inr")
                    .setDescription(billDetailsDTO.getCustomerName())
                    .build();
            PaymentIntent intent = PaymentIntent.create(createParams);
            // Confirm the Payment Intent
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(tokenId)
                    .build();
            PaymentIntent confirmedIntent = intent.confirm(confirmParams);
            // Check if the payment is successful
            if ("succeeded".equals(confirmedIntent.getStatus())) {
                PaymentTransaction payment = new PaymentTransaction();
                payment.setAmountPaid(billDetailsDTO.getTotalAmount());
                payment.setCustomerId(billDetailsDTO.getCustomerId());
                payment.setPaymentDate(LocalDateTime.now());
                // Save the payment transaction to the database
                return paymentTransactionRepository.save(payment);
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
