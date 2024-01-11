package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, BigInteger> {
    Connection findByEmailOrCustomerId(String email, String customerId);

    Connection save(Connection connection);

    boolean existsByEmail(String emailAddress);

    boolean existsByPhoneNumber(String phoneNumber);


    boolean existsByCustomerId(String customerId);

    Connection findByCustomerId(String customerId);

    List<Connection> findByStatusIn(List<Object> list);

    List<Connection> findByTehsilAndStatus(String tehsil, ConnectionStatus connectionStatus);

    @Modifying
    @Transactional
    @Query("UPDATE Connection c SET c.password = :newPassword WHERE c.customerId = :customerId")
    int resetPasswordByCustomerId(@Param("customerId") String customerId, @Param("newPassword") String newPassword);

    List<Connection> findByStatus(ConnectionStatus connectionStatus);

    @Modifying
    @Query("UPDATE Connection c SET c.fullName = :name, c.fathersName = :fathersName, c.updateCount = c.updateCount + 1 WHERE c.customerId = :customerId AND c.updateCount < 2")
    int updateFullNameAndFathersNameByCustomerId(
            @Param("customerId") String customerId,
            @Param("name") String fullName,
            @Param("fathersName") String fathersName
    );
   @Query("SELECT c.updateCount FROM Connection c WHERE c.customerId = :customerId")
   Integer findUpdateCountByCustomerId(@Param("customerId") String customerId);
    @Modifying
    @Query("UPDATE Connection c SET c.phoneNumber = :number, c.email = :email, c.updateContactCount = c.updateContactCount + 1 WHERE c.customerId = :customerId AND c.updateContactCount < 2")
    int updatePhoneNumberAndEmailByCustomerId(
            @Param("customerId") String customerId,
            @Param("number") String phoneNumber,
            @Param("email") String fathersName
    );
    @Query("SELECT c.updateContactCount FROM Connection c WHERE c.customerId = :customerId")
    int findUpdateContactCountByCustomerId(@Param("customerId") String customerId);
    @Modifying
    @Transactional
    @Query("UPDATE Connection c SET c.password = :newPassword WHERE c.customerId = :customerId")
    int updatePasswordByCustomerId(@Param("customerId") String customerId, @Param("newPassword") String newPassword);
    @Query("SELECT c.password FROM Connection c WHERE c.customerId = :customerId")
    String findPasswordByCustomerId(@Param("customerId") String customerId);
    @Query("SELECT COUNT(c) FROM Connection c WHERE c.status = 'ACTIVE'")
    int countActiveConnections();
}
