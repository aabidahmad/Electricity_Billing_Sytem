package com.ElectricityAutomationInitiative.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginDTO implements Serializable {
    private String EmployeeId;
    private String password;
}
