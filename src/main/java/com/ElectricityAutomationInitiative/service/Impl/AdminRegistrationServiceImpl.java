package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.Admin;
import com.ElectricityAutomationInitiative.entity.EmployeeID;
import com.ElectricityAutomationInitiative.entity.UserRole;
import com.ElectricityAutomationInitiative.payload.AdminRegistrationDTO;
import com.ElectricityAutomationInitiative.repository.AdminRepository;
import com.ElectricityAutomationInitiative.repository.EmployeeIdRepository;
import com.ElectricityAutomationInitiative.repository.RoleRepository;
import com.ElectricityAutomationInitiative.service.AdminRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminRegistrationServiceImpl implements AdminRegistrationService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private EmployeeIdRepository employeeIdRepository;
    private RoleRepository roleRepository;
    @Autowired
    public AdminRegistrationServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder
    ,EmployeeIdRepository employeeIdRepository,RoleRepository roleRepository) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.employeeIdRepository=employeeIdRepository;
        this.roleRepository=roleRepository;
    }
    public EmployeeID getEmployeeIDByEmployeeId(String employeeId) {
        return employeeIdRepository.findByEmployeeId(employeeId);
    }
    public Object registerAdmin(AdminRegistrationDTO adminRegistrationDTO) {
        // Check if the employee with the provided EmpId exists
        EmployeeID employeeID = getEmployeeIDByEmployeeId(adminRegistrationDTO.getEmployeeId());

        Admin newadmin = new Admin();
        if (employeeID == null) {
            // Handle case where EmployeeID is not found
            return "Employee Not Found ";
        } else if (adminRepository.existsByEmployeeId(adminRegistrationDTO.getEmployeeId())) {
            return false;
        }
        // Create an Admin entity and populate it from DTO
        else {
            newadmin.setUsername(adminRegistrationDTO.getUsername());
            newadmin.setPassword(passwordEncoder.encode(adminRegistrationDTO.getPassword()));

            UserRole adminRole = roleRepository.findRoleByRoleName("ROLE_ADMIN");

            //System.out.println("Role is "+adminRole);
            if (adminRole == null) {
                // Create the admin role if it doesn't exist
                //	System.out.println(adminRole.getRoleName());
                adminRole = new UserRole("ROLE_ADMIN");
                adminRole = roleRepository.save(adminRole);
            }
            // Add the admin role to the admin's roles
            newadmin.getRoles().add(adminRole);
            newadmin.setFullName(adminRegistrationDTO.getFullName());
            newadmin.setEmployeeId(employeeID.getEmployeeId());
            newadmin.setContactNumber(adminRegistrationDTO.getContactNumber());
            newadmin.setEmail(adminRegistrationDTO.getEmail());
            newadmin.setAddress(adminRegistrationDTO.getAddress());
            newadmin.setWorkplace(adminRegistrationDTO.getWorkplace());
            newadmin.setDesignation(adminRegistrationDTO.getDesignation());
            // Save the admin entity in the database

            adminRepository.save(newadmin);
            return true;
        }
    }
    @Override
    public boolean isEmailUnique(String emailAddress) {
        return !adminRepository.existsByEmail(emailAddress);
    }

}
