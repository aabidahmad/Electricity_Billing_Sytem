package com.ElectricityAutomationInitiative.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private String emailOrConnectionId;
    private String password;
    public LoginDTO(){

    }
}
