package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.EmployeeID;
import com.ElectricityAutomationInitiative.payload.AdminRegistrationDTO;

public interface AdminRegistrationService {
    EmployeeID getEmployeeIDByEmployeeId(String employeeId);
    Object registerAdmin(AdminRegistrationDTO adminRegistrationDTO);

    boolean isEmailUnique(String emailAddress);
}
