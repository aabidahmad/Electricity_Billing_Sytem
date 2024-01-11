package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.EmployeeID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeIdRepository extends JpaRepository<EmployeeID, Long> {

     EmployeeID findByEmployeeId(String employeeId);
}
