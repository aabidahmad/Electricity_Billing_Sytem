package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.Admin;
import com.ElectricityAutomationInitiative.entity.Bill;
import com.ElectricityAutomationInitiative.payload.AdminDetails;
import com.ElectricityAutomationInitiative.payload.ConnectionResponse;

import java.util.List;

public interface AdminService {

    public List<ConnectionResponse> getPendingConnectionDTOs(String workplace);

    public boolean activateConnection(String connectionId);

    boolean rejectConnection(String connectionId);

    public AdminDetails fetchDetails(String EmpoyeeId);


    List<Bill> findPaidBillsByTehsil(String tehsil);

    public AdminDetails updateAdminDetails(String employeeId, AdminDetails updatedAdminDetails);

    Admin getByEmailOrId(String emailOrId);

    boolean resetPassword(String employeeId, String encodedPassword);

}

