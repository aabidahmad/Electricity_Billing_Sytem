package com.ElectricityAutomationInitiative.util;

import com.ElectricityAutomationInitiative.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CustomIdGenerator  {
    private String generateRandomId() {
        int length = 12;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomId = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char character = characters.charAt(index);
            randomId.append(character);
        }
        return randomId.toString();

    }
    public String generateUniqueId() {

        String generatedId =generateRandomId();
        System.out.println("generated Id Is "+generatedId);
        while (!isUniqueId(generatedId)) {
            generatedId = generateRandomId();
        }
        return generatedId;
    }



    @Autowired
    ConnectionService connectionService;
    private boolean isUniqueId(String generatedId) {
        System.out.println(generatedId);
        if(connectionService.isCustomerIdUnique(generatedId)==false) {
            System.out.println(generatedId);
            return false;
        }
        else
            return true;
    }
}

