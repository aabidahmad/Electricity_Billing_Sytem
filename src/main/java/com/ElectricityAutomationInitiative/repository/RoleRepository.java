package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<UserRole ,Long> {
    UserRole findRoleByRoleName(String admin);
}

