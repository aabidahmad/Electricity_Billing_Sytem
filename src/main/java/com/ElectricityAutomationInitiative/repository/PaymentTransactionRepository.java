package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long > {
}
