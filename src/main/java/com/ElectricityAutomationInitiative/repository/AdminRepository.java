package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.Admin;
import com.ElectricityAutomationInitiative.entity.EmployeeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

  Admin findByEmployeeId(String employeeId);
  boolean existsByEmployeeId(String employeeId);

    boolean existsByEmail(String emailAddress);

    Admin getByEmailOrEmployeeId(String emailOrId, String emailOrId1);
    @Transactional
    @Modifying
    @Query("UPDATE Admin a SET a.password = :newPassword WHERE a.employeeId = :employeeId")
    int resetPasswordByEmployeeId(@Param("employeeId") String employeeId, @Param("newPassword") String newPassword);


    // You can add more custom query methods if needed
}

