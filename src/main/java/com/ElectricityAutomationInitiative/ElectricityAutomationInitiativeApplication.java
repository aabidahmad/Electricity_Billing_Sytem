package com.ElectricityAutomationInitiative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication
@ComponentScan(basePackages = "com.ElectricityAutomationInitiative")
public class ElectricityAutomationInitiativeApplication {
	public static void main(String[] args) {
		SpringApplication.run(ElectricityAutomationInitiativeApplication.class, args);
		System.out.println();
	}
}
