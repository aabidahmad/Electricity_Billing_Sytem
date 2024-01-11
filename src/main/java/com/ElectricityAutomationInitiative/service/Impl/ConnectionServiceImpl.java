package com.ElectricityAutomationInitiative.service.Impl;


import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.ConnectionStatus;
import com.ElectricityAutomationInitiative.entity.Notification;
import com.ElectricityAutomationInitiative.entity.UserRole;
import com.ElectricityAutomationInitiative.payload.ConnectionDTO;
import com.ElectricityAutomationInitiative.payload.ConnectionDetails;
import com.ElectricityAutomationInitiative.payload.ConnectionResponse;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.repository.NotificationRepository;
import com.ElectricityAutomationInitiative.repository.RoleRepository;
import com.ElectricityAutomationInitiative.service.ConnectionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ModelMapper modelMapper;
    private final Map<String, ConnectionDTO> temporaryConnections;

    private RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    public ConnectionServiceImpl(ConnectionRepository connectionRepository, ModelMapper modelMapper,
    RoleRepository roleRepository) {
        this.connectionRepository = connectionRepository;
        this.modelMapper=modelMapper;
        this.temporaryConnections = new HashMap<>();
        this.roleRepository=roleRepository;
    }

    @Override
    public boolean isCustomerIdUnique(String customerId) {
        return !connectionRepository.existsByCustomerId(customerId);
    }

    @Override
    public boolean isEmailUnique(String emailAddress)
    {
        return !connectionRepository.existsByEmail(emailAddress);
    }

    @Override
    public boolean isPhoneNumberUnique(String phoneNumber) {
        return !connectionRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    public Connection registerConnection(ConnectionDTO connectionDTO) {
        Connection connection = new Connection();
        connection.setFullName(connectionDTO.getFullName());
        connection.setFathersName(connectionDTO.getFathersName());
        connection.setAddress(connectionDTO.getAddress());
        connection.setEmail(connectionDTO.getEmail());
        connection.setTehsil(connectionDTO.getTehsil());
        connection.setDistrict(connectionDTO.getDistrict());
        connection.setState(connectionDTO.getState());
        connection.setPhoneNumber(connectionDTO.getPhoneNumber());
        connection.setPostalCode(connectionDTO.getPostalCode());
        connection.setCustomerId(connectionDTO.getCustomerId());
        connection.setPassword(connectionDTO.getPassword());
        connection.setCity(connectionDTO.getCity());
        UserRole userRole = roleRepository.findRoleByRoleName("ROLE_USER");
        System.out.println("userRole "+userRole);

        if (userRole == null) {
            // Create the role if it doesn't exist
            userRole = new UserRole("ROLE_USER");
            userRole = roleRepository.save(userRole);
        }
// Assign the role to the user
        connection.getRoles().add(userRole);
// Save the updated user with the assigned role
        connectionRepository.save(connection);

        return connection;
    }


    @Override
    public boolean existsByCustomerId(String customerId) {
       return  !connectionRepository.existsByCustomerId(customerId);
    }
    @Override
    public ConnectionDTO existsById(String customerId){
        Connection byCustomerId = connectionRepository.findByCustomerId(customerId);
        return mapToDto(byCustomerId);
    }

    @Override
    public Connection getConnectionByCustomerId(String customerId) {
        Connection connection = connectionRepository.findByCustomerId(customerId);
         return connection;
    }
    @Override
    public Connection getConnectionByConnectionId(String customerId) {
        Connection connection = connectionRepository.findByCustomerId(customerId);
        return connection;

    }
    public ConnectionDTO mapToDto(Connection connection){
        ConnectionDTO map = modelMapper.map(connection, ConnectionDTO.class);
        return map;
    }

   public Connection mapToEntity(ConnectionDTO connectionDTO){
        Connection connection = modelMapper.map(connectionDTO, Connection.class);
        return connection;
    }
   private ConnectionDetails mapToDetailDTO(Connection connection) {
       ConnectionDetails connectionDetails = new ConnectionDetails();
       connectionDetails.setFullName(connection.getFullName());
       connectionDetails.setFathersName(connection.getFathersName());
       connectionDetails.setCustomerId(connection.getCustomerId());
       connectionDetails.setEmail(connection.getEmail());
       connectionDetails.setTehsil(connection.getTehsil());
       connectionDetails.setDistrict(connection.getDistrict());
       connectionDetails.setAddress(connection.getAddress());
       connectionDetails.setState(connection.getState());
       connectionDetails.setPhoneNumber(connection.getPhoneNumber());
       connectionDetails.setPostalCode(connection.getPostalCode());
       connectionDetails.setStatus(connection.getStatus());
       return connectionDetails;
   }
    @Override
    public ConnectionDetails getConnectionDetailsByCustomerId(String customerId) {
        Connection connection = connectionRepository.findByCustomerId(customerId);
        return mapToDetailDTO(connection);
    }

    @Override
    public boolean resetPassword(String customerId, String encodedPassword) {
        int i = connectionRepository.resetPasswordByCustomerId(customerId, encodedPassword);
        if(i>0){
           System.out.println("done");
           return true;

       }
       else{
           System.out.println("Not done");
           return false;
       }
    }
    @Override
    public Connection getByEmailOrCustomerId(String email, String customerId){
        return connectionRepository.findByEmailOrCustomerId(email,customerId);
    }
    @Override
    public List<Notification> getNotifications(String area){
        List<Notification> byArea = notificationRepository.findByArea(area);
        return byArea;
    }

    private ConnectionDTO mapToDTO(Connection connection){
        ConnectionDTO connectionDTO=new ConnectionDTO();
        connectionDTO.setFullName(connection.getFullName());
        connectionDTO.setFathersName(connection.getFathersName());
        connectionDTO.setAddress(connection.getAddress());
        connectionDTO.setEmail(connection.getEmail());
        connectionDTO.setPhoneNumber(connection.getPhoneNumber());
        connectionDTO.setTehsil(connection.getTehsil());
        connectionDTO.setCustomerId(connection.getCustomerId());
        connectionDTO.setCity(connection.getCity());
        connectionDTO.setPostalCode(connection.getPostalCode());
        connectionDTO.setDistrict(connection.getDistrict());
        connectionDTO.setState(connection.getState());
        connectionDTO.setStatus(connection.getStatus());
        return connectionDTO;
    }
    @Override
    public List<ConnectionDTO> getAllPendingConnection() {
        List<Connection> byStatus = connectionRepository.findByStatus(ConnectionStatus.PENDING);
        List<ConnectionDTO> connectionDTOs = byStatus.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return connectionDTOs;
    }
    @Override
    public List<ConnectionDTO> getAllActiveConnection() {
        List<Connection> byStatus = connectionRepository.findByStatus(ConnectionStatus.ACTIVE);
        List<ConnectionDTO> connectionDTOs = byStatus.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return connectionDTOs;
    }
    @Override
    public String updateDetails(String customerId, String name, String fathersName) {
        int updatedRows = connectionRepository.updateFullNameAndFathersNameByCustomerId(customerId, name, fathersName);
        int updatedCount = connectionRepository.findUpdateCountByCustomerId(customerId);
        if (updatedRows > 0 && updatedCount <= 2) {

            return "Details Updated Successfully  "+updatedCount;
        } else if (updatedCount == 2) {
            return "You have consumed the maximum number of allowed updates";
        } else {
            return "Error in updating details";
        }
    }

    @Override
    public String updateContactDetails(String customerId, String phoneNumber, String email) {
        int updatedRows = connectionRepository.updatePhoneNumberAndEmailByCustomerId(customerId,phoneNumber,email);
        int updatedCount = connectionRepository.findUpdateContactCountByCustomerId(customerId);

        if (updatedRows > 0 && updatedCount <= 2) {
            return "Details Updated Successfully  "+updatedCount;
        } else if (updatedCount == 2) {
            return "You have consumed the maximum number of allowed updates";
        } else {
            return "Error in updating details";
        }

  }
    @Override
    public int updatePasswordDetails(String customerId, String encodedPassword, String encodedNewPassword) {
        String passwordByCustomerId = connectionRepository.findPasswordByCustomerId(encodedPassword);
        if(passwordByCustomerId==encodedNewPassword){
            int i = connectionRepository.updatePasswordByCustomerId(customerId, encodedNewPassword);
            System.out.println("password Status is "+i);
            return i;
        }
        else{
            return 0;
        }
    }


}

