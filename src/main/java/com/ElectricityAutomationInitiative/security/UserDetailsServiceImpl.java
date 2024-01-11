package com.ElectricityAutomationInitiative.security;

import com.ElectricityAutomationInitiative.entity.Admin;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.repository.AdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connection connection = connectionRepository.findByEmailOrCustomerId(username, username);
        Admin admin = adminRepository.findByEmployeeId(username);
      //  System.out.println("Employee is "+admin.getFullName());
        if (connection != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            // Assuming roles are stored in a property of UserDao (e.g., user.getRoles())
            connection.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

            return new org.springframework.security.core.userdetails.User(
                    connection.getCustomerId(),
                    connection.getPassword(),
                    authorities
            );

        } else if (admin != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            // Assuming roles are stored in a property of Admin (e.g., admin.getRoles())
            admin.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getRoleName())));

            return new org.springframework.security.core.userdetails.User(
                    admin.getEmployeeId(),
                    admin.getPassword(),
                    authorities
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}

