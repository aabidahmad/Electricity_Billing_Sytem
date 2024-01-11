package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.Notification;
import com.ElectricityAutomationInitiative.payload.AdminDetails;
import com.ElectricityAutomationInitiative.payload.ConnectionDTO;
import com.ElectricityAutomationInitiative.payload.ConnectionDetails;

import java.util.List;

public interface ConnectionService {

   public boolean isCustomerIdUnique (String id);

    boolean isEmailUnique(String emailAddress);

    boolean isPhoneNumberUnique(String phoneNumber);

    Connection registerConnection(ConnectionDTO connectionDTO);





//    ConnectionDTO findByCustomerId(String username);

 boolean existsByCustomerId(String customerId);

    ConnectionDTO existsById(String customerId);

    Connection getConnectionByCustomerId(String customerId);

    Connection getConnectionByConnectionId(String customerId);
    ConnectionDTO mapToDto(Connection connection);
    public Connection mapToEntity(ConnectionDTO connectionDTO);
    public ConnectionDetails getConnectionDetailsByCustomerId(String customerId);

    boolean resetPassword(String customerId, String encodedPassword);

    Connection getByEmailOrCustomerId(String email, String customerId);

    List<Notification> getNotifications(String area);

    List<ConnectionDTO> getAllPendingConnection();

    List<ConnectionDTO> getAllActiveConnection();

    String updateDetails(String customerId, String name, String fathersName);

    String updateContactDetails(String customerId, String phoneNumber, String email);

    public int updatePasswordDetails(String customerId, String encodedPassword, String encodedNewPassword);

}




