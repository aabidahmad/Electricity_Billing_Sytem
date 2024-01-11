package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.Admin;
import com.ElectricityAutomationInitiative.entity.Bill;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.ConnectionStatus;
import com.ElectricityAutomationInitiative.payload.AdminDetails;
import com.ElectricityAutomationInitiative.payload.ConnectionResponse;
import com.ElectricityAutomationInitiative.repository.AdminRepository;
import com.ElectricityAutomationInitiative.repository.BillRepository;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    ConnectionRepository connectionRepository;

    @Autowired
    BillRepository billRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public List<ConnectionResponse> getPendingConnectionDTOs(String workplace) {
        List<Connection> pendingConnections = connectionRepository.findByTehsilAndStatus(workplace,ConnectionStatus.PENDING);

        List<ConnectionResponse> pendingConnectionDTOs = pendingConnections.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return pendingConnectionDTOs;
    }


    @Override
    public boolean activateConnection(String connectionId) {
        boolean b=false;
        Connection connection = connectionRepository.findByCustomerId(connectionId);
        if (connection == null) {
            new EntityNotFoundException("Connection not found");
        }
        else {
            b=true;
            connection.setStatus(ConnectionStatus.ACTIVE);
            connectionRepository.save(connection);
        }
        return b;
    }
    @Override
    public boolean rejectConnection(String connectionId) {
        Connection connection = connectionRepository.findByCustomerId(connectionId);
        if (connection == null) {
            throw new EntityNotFoundException("Connection not found");
        }

        // Update the status to REJECTED
        connection.setStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
        // Delete the record
        connectionRepository.delete(connection);

        return true; // You can return true to indicate success
    }

    @Override
    public AdminDetails fetchDetails(String employeeId) {
        AdminDetails adminDetails=new AdminDetails();
        Admin admin = adminRepository.findByEmployeeId(employeeId);
        adminDetails.setFullName(admin.getFullName());
        adminDetails.setAddress(admin.getAddress());
        adminDetails.setEmail(admin.getEmail());
        adminDetails.setContactNumber(admin.getContactNumber());
        adminDetails.setWorkplace(admin.getWorkplace());
        adminDetails.setEmployeeId(admin.getEmployeeId());
       adminDetails.setDesignation(admin.getDesignation());
        return adminDetails;
    }

    public ConnectionResponse toDTO(Connection connection) {
        ConnectionResponse dto = new ConnectionResponse();
        dto.setCustomerId(connection.getCustomerId());
        dto.setFullName(connection.getFullName());
        dto.setAddress(connection.getAddress());
        dto.setTehsil(connection.getTehsil());
        dto.setDistrict(connection.getDistrict());
        dto.setState(connection.getState());
        dto.setPostalCode(connection.getPostalCode());

        //dto.getPdfContent(connection.getUserDocs());
        // Set any other properties you need to copy

        return dto;
    }
    private Admin mapTOEntity(AdminDetails adminDetails){
        Admin admin=new Admin();
        admin.setFullName(adminDetails.getFullName());
        admin.setEmail(adminDetails.getEmail());
        admin.setAddress(adminDetails.getAddress());
        admin.setContactNumber(adminDetails.getContactNumber());
        return admin;
}
private AdminDetails mapToDTO(Admin admin){
     AdminDetails adminDetails=new AdminDetails();
     adminDetails.setFullName(admin.getFullName());
     adminDetails.setEmail(admin.getEmail());
     adminDetails.setAddress(admin.getAddress());
     adminDetails.setContactNumber(admin.getContactNumber());
     return adminDetails;

}

    @Override
    public List<Bill> findPaidBillsByTehsil(String tehsil) {
        List<Bill> paidBillsByTehsil = billRepository.findByTehsilAndPaidIsTrue(tehsil);
        System.out.println("Size is "+paidBillsByTehsil.size());


        return null;
    }

    @Override
    public AdminDetails updateAdminDetails(String employeeId, AdminDetails updatedAdminDetails) {
        // Retrieve the existing Admin entity by employeeId
        Admin admin = adminRepository.findByEmployeeId(employeeId);

        if (admin != null) {
            // Update the fields with non-null values from updatedAdminDetails
            if (updatedAdminDetails.getFullName() != null) {
                admin.setFullName(updatedAdminDetails.getFullName());
            }
            if (updatedAdminDetails.getContactNumber() != null) {
                admin.setContactNumber(updatedAdminDetails.getContactNumber());
            }
            if (updatedAdminDetails.getEmail() != null) {
                admin.setEmail(updatedAdminDetails.getEmail());
            }
            if (updatedAdminDetails.getAddress() != null) {
                admin.setAddress(updatedAdminDetails.getAddress());
            }
            // Save the updated Admin entity
           return mapToDTO(adminRepository.save(admin));
        } else {
            return null;
        }
    }

    @Override
    public Admin getByEmailOrId(String emailOrId) {
       return adminRepository.getByEmailOrEmployeeId(emailOrId,emailOrId);
    }
    @Override
    public boolean resetPassword(String customerId, String encodedPassword) {

    int i = adminRepository.resetPasswordByEmployeeId(customerId, encodedPassword);
        if(i>0){
            System.out.println("done");
            return true;
        }
        else{
            System.out.println("Not done");
            return false;
        }
    }
}
