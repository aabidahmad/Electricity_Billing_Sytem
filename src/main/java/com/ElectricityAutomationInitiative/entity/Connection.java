package com.ElectricityAutomationInitiative.entity;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


// Annotate the class as an entity and specify the table name (if needed)
@Data
@Entity
@Table(name = "connections")
public class Connection implements Serializable {

    // Other fields and annotations remain the same

    // Implement the Serializable interface
    private static final long serialVersionUID = 1L;

    // Define an ID field and annotate it as the primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long id;
    // Define fields for personal information with constraints
    @NotBlank
    @Size(max = 100)
    private String fullName;
    @NotBlank
    @Size(max = 100)
    private String fathersName;

    @Size(min = 10, max = 15)
    @Pattern(regexp = "[0-9]+")
    @Column(unique = true)
    private String phoneNumber;
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true)
    private String email;

    // Define fields for address details
    @NotBlank
    @Size(max = 100)
    private String Address;

    @NotBlank
    @Size(max = 50)
    private String tehsil;
    @NotBlank
    @Size(max = 50)
    private String district;
    @NotBlank
    @Size(max = 50)
    private String city;


    @NotBlank
    @Size(max = 50)
    private String state;
    @NotBlank
    @Size(max = 10)
    private String postalCode;

    @Column(unique = true, nullable = false)
    private String customerId;
    @Column(unique = true, nullable = false)
    @Size(min = 8)
    private String password;


    // One connection can have multiple associated documents
    @OneToMany(mappedBy = "connection", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FileEntity> fileEntity = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_mapping",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false )
    private ConnectionStatus status = ConnectionStatus.PENDING;
     @Column(nullable = false)
     private int updateCount = 0;
    public void incrementUpdateCount() {
        this.updateCount++;
    }
    @Column(nullable = false)
    private int updateContactCount = 0;

    public void incrementUpdateContactCount() {
        this.updateContactCount++;
    }
}