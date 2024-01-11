package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BillRepository extends JpaRepository<Bill,Long> {
    Bill findTopByConnectionCustomerIdOrderByDueDateDesc(String customerId);
    Bill findTopByConnectionCustomerIdOrderByBillingDateDesc(String customerId);
    List<Bill> findByTehsilAndPaidIsTrue(String tehsilReference);
    List<Bill> findByBillingDateBetweenAndTehsil(LocalDate startOfMonth, LocalDate endOfMonth, String tehsil);


    List<Bill> findByConnection_CustomerIdAndPaidIsTrue(String customerId);
        @Query("SELECT b.connection.district, SUM(b.paidAmount) FROM Bill b WHERE b.paid = true GROUP BY b.connection.district")
        List<Object[]> calculateTotalPaidAmountByDistrict();

    @Query("SELECT FUNCTION('YEAR', b.billingDate), FUNCTION('MONTH', b.billingDate), SUM(b.paidAmount) " +
            "FROM Bill b " +
            "WHERE b.paid = true " +
            "GROUP BY FUNCTION('YEAR', b.billingDate), FUNCTION('MONTH', b.billingDate)")
    List<Object[]> calculateTotalPaidAmountByYearAndMonth();
    List<Bill> findByPaidFalse();
    List<Bill> findByPaidTrue();
    @Query("SELECT c.district, SUM(b.totalAmount) FROM Bill b JOIN b.connection c " +
            "WHERE b.paid = false GROUP BY c.district")
    List<Object[]> calculateUnpaidRevenueByDistrict();
    @Query("SELECT NEW map(FUNCTION('YEAR', u.billingDate) as year, FUNCTION('MONTH', u.billingDate) as month, SUM(u.totalAmount) as totalAmount) " +
            "FROM Bill u " +
            "WHERE u.paid = false " +
            "GROUP BY FUNCTION('YEAR', u.billingDate), FUNCTION('MONTH', u.billingDate)")
    List<Map<String, Object>> findUnpaidRevenueGroupedByMonthAndYear();


    @Query("SELECT c.district, COUNT(b) FROM Bill b JOIN b.connection c GROUP BY c.district")
    List<Object[]> getBillCountByDistrict();

    @Query("SELECT MONTH(b.billingDate) as month, YEAR(b.billingDate) as year, COUNT(b) as billCount " +
            "FROM Bill b " +
            "GROUP BY MONTH(b.billingDate), YEAR(b.billingDate)")
    List<Object[]> getBillCountByMonth();
        @Query("SELECT b FROM Bill b " +
                "WHERE FUNCTION('YEAR', b.paymentDate) = FUNCTION('YEAR', CURRENT_DATE()) " +
                "AND (FUNCTION('MONTH', b.paymentDate) = FUNCTION('MONTH', CURRENT_DATE()) " +
                "OR (FUNCTION('MONTH', b.paymentDate) < FUNCTION('MONTH', CURRENT_DATE()) " +
                "AND FUNCTION('YEAR', b.paymentDate) = FUNCTION('YEAR', CURRENT_DATE())))")
        List<Bill> findCurrentOrPreviousMonthsPaidBills();

    List<Bill> findByConnection_CustomerIdAndPaidIsFalse(String customerId);
    @Query("SELECT SUM(b.paidAmount) FROM Bill b")
    Double getTotalPaidAmount();
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.paid = true")
    int countAllPaidBills();
    @Query("SELECT SUM(b.paidAmount) FROM Bill b WHERE b.connection.customerId = :customerId")
    Double getTotalPaidAmountByCustomerId(String customerId);
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.connection.customerId = :customerId AND b.paid = true")
    long countPaidBillsByCustomerId(String customerId);
    @Query("SELECT COUNT(b) FROM Bill b WHERE b.connection.customerId = :customerId AND b.paid = false")
    long countUnPaidBillsByCustomerId(String customerId);
}
