package com.ElectricityAutomationInitiative.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String customerId;
    @Column(nullable = false)
    private String complaintText;
    @Column(nullable = false)
    private String tehsil;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    // Constructors, getters, and setters
}
