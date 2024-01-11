package com.ElectricityAutomationInitiative.repository;
import com.ElectricityAutomationInitiative.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<FileEntity, Long> {
    @Query("SELECT f.data FROM FileEntity f WHERE f.connection.customerId = :customerId")
    byte[] findDataByCustomerId(@Param("customerId") String customerId);
}



