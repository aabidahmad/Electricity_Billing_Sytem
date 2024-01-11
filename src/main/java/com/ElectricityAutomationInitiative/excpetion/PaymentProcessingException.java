package com.ElectricityAutomationInitiative.excpetion;

import com.stripe.exception.StripeException;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String errorProcessingPaymentWithStripe, StripeException ex) {
      super( errorProcessingPaymentWithStripe,ex);
    }
}
