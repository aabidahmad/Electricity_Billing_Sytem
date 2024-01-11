package com.ElectricityAutomationInitiative.entity;

import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;
import javax.persistence.*;

@Entity
@Table(name = "files")
@Data
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId") // Foreign key column name and referenced column name
    private Connection connection;

    @Lob
    private byte[] data;

    // getters and setters
}