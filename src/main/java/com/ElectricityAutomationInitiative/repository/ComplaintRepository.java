package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
    List<Complaint> findByTehsil(String tehsil);
    long count();
}
