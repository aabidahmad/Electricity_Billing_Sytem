package com.ElectricityAutomationInitiative.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "bill_paid_date")
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "customer_id", nullable = false,referencedColumnName = "customerId")
    private Connection connection;

    @Column(nullable = false)
    private LocalDate billingDate;


    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private double totalAmount;

    @Column(nullable = false)
    private double paidAmount;
    // New field to store the tehsil value from Connection
    @Column(name = "tehsil")
    private String tehsil;

    @Column(nullable = false)
    private boolean paid;
    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PaymentTransaction paymentTransaction;

}

