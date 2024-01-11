package com.ElectricityAutomationInitiative;

import java.util.Random;

public class EmployeeIdGenerator {

    public static String generateEmployeeId() {
        String prefix = "EMP";
        String numbers = generateRandomNumbers(9); // Generate 9 random digits

        return prefix + numbers;
    }

    private static String generateRandomNumbers(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        String employeeId = generateEmployeeId();
        System.out.println("Generated Employee ID: " + employeeId);
        //EMP881222927
    }
}

