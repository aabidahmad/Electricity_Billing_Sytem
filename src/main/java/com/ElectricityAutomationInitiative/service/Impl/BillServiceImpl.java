package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.Bill;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.ConnectionStatus;
import com.ElectricityAutomationInitiative.payload.BillDetailsDTO;
import com.ElectricityAutomationInitiative.repository.BillRepository;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.service.BillService;
import com.ElectricityAutomationInitiative.service.StationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {
    private final BillRepository billRepository;
    private final ConnectionRepository connectionRepository;
    private final StationDetailsService stationDetailsService;

    @Autowired
    public BillServiceImpl(BillRepository billRepository,
                           ConnectionRepository connectionRepository,
                           StationDetailsService stationDetailsService) {
        this.billRepository = billRepository;
        this.connectionRepository = connectionRepository;
        this.stationDetailsService=stationDetailsService;
    }

    @Override
    @Scheduled(cron = "0 0 0 20 * ?") // Run at midnight (00:00) on the 20th of every month
    public void generateBillForCustomersOn20th() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDayOfMonth=currentDate.withDayOfMonth(currentDate.lengthOfMonth());;
        currentDate.getDayOfMonth();
        // Check if today's date is after the 20th of the month
        if (currentDate.getDayOfMonth() == 20) {
            // If today is after the 20th, set the billing date to the 20th of the next month
            currentDate = currentDate.plusMonths(1).withDayOfMonth(20);
        } else {
            // Set the due date to the last day of the current month
             lastDayOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
        }
        // Get all connections for which the bill needs to be generated
        List<Connection> connections = connectionRepository.findAll();
        // Loop through each connection and generate the bill
        for (Connection connection : connections) {

            if (connection.getStatus() == ConnectionStatus.ACTIVE) {
                // Check if the customer has an unpaid bill
                Bill previousUnpaidBill = billRepository.findTopByConnectionCustomerIdOrderByDueDateDesc(connection.getCustomerId());
                // Calculate the previous unpaid amount if there is any
                double previousUnpaidAmount = (previousUnpaidBill != null) ? previousUnpaidBill.getTotalAmount() : 0.0;
                double totalBillAmount = calculateBillAmountForCustomer(connection);
                if (previousUnpaidAmount > 0) {

                    double penaltyRate = stationDetailsService.getPenaltyAmount(connection.getTehsil());
                    double penalty = penaltyRate / 100;
                    double penaltyAmount = previousUnpaidAmount * penalty;
                    totalBillAmount = previousUnpaidAmount + penaltyAmount + totalBillAmount;
                }

                Bill bill = new Bill();
                bill.setConnection(connection);
                bill.setBillingDate(currentDate);
                bill.setDueDate(lastDayOfMonth); // Assuming the due date is 30 days from the billing date.
                bill.setTotalAmount(totalBillAmount);
                bill.setTehsil(connection.getTehsil());
                bill.setPaidAmount(0.0); // Initially, the paid amount is 0.
                bill.setPaid(false); // Initially, the bill is unpaid.
                billRepository.save(bill);
            }
        }
    }

    // Add your business logic to calculate the bill amount for the customer based on billing plan, usage, etc.
    @Override
    public double calculateBillAmountForCustomer(Connection connection) {
        double amount = stationDetailsService.getMonthlyBillAmount(connection.getTehsil());
        return amount; // Replace with your actual logic.
    }

    @Override
    public Object getBillDetailsByCustomerId(String customerId, boolean paid) {
        // Get the latest unpaid bill for the customer
        Bill latestUnpaidBill = billRepository.findTopByConnectionCustomerIdOrderByBillingDateDesc(customerId);

        System.out.println(" latest bill "+latestUnpaidBill);
        if (latestUnpaidBill == null) {
            return "No Unpaid Bill Found!";
        } else if (latestUnpaidBill.getConnection().getStatus() != ConnectionStatus.ACTIVE) {
            return "Customer is not active.";
        }
//        else if (latestUnpaidBill.isPaid()) {
//            return "Bill Already Paid!";
//        }
        else {
            // Check if the due date is in the future
            LocalDate currentDate = LocalDate.now();
            LocalDate dueDate = latestUnpaidBill.getDueDate();

            return mapToDto(latestUnpaidBill);
        }
    }
    @Override
    public BillDetailsDTO saveBill(BillDetailsDTO billDetailsDTO) {
        // Fetch the existing bill from the database using the customer ID
        Bill existingBill = billRepository.findTopByConnectionCustomerIdOrderByDueDateDesc(billDetailsDTO.getCustomerId());
        if (existingBill == null) {
            throw new EntityNotFoundException("Bill not found for customer ID: " + billDetailsDTO.getCustomerId());
        }
        // Update the bill properties with the new payment details
        existingBill.setPaid(true);
        existingBill.setPaidAmount(billDetailsDTO.getPaidAmount());
        existingBill.setTotalAmount(0.0);
        // Save the updated bill back to the database
        existingBill = billRepository.save(existingBill);
        // Convert the updated bill to a BillDetailsDTO and return it
        return mapToDto(existingBill);
    }



        @Override
        public List<BillDetailsDTO> findPaidBillsByTehsil(String tehsil) {
            List<Bill> paidBillsByTehsil = billRepository.findByTehsilAndPaidIsTrue(tehsil);
            System.out.println("Size is "+paidBillsByTehsil.size());
            List<BillDetailsDTO> billDetailsDTOList = paidBillsByTehsil.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
            return billDetailsDTOList;
        }
    @Override
    public List<BillDetailsDTO> findAllPaidBillsByTehsil(String tehsil) {
        List<Bill> paidBillsByTehsil = billRepository.findByTehsilAndPaidIsTrue(tehsil);
        System.out.println("Size is "+paidBillsByTehsil.size());
        List<BillDetailsDTO> billDetailsDTOList = paidBillsByTehsil.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;


    }

    @Override
    public List<BillDetailsDTO> findAllPaidBillsByCustomerId(String customerId) {
        List<Bill> paidBillsByTehsil = billRepository.findByConnection_CustomerIdAndPaidIsTrue(customerId);
        System.out.println("Size is "+paidBillsByTehsil.size());
        List<BillDetailsDTO> billDetailsDTOList = paidBillsByTehsil.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;
    }

    public List<BillDetailsDTO> getCurrentMonthBills(String tehsil) {
        // Get the current year and month
        YearMonth currentYearMonth = YearMonth.now();

        // Calculate the start and end dates of the current month
        LocalDate startOfMonth = currentYearMonth.atDay(1);
        LocalDate endOfMonth = currentYearMonth.atEndOfMonth();

        // Use the repository method to fetch bills for the current month
        List<Bill> byBillingDateBetweenAndTehsil = billRepository.findByBillingDateBetweenAndTehsil(startOfMonth, endOfMonth, tehsil);
        List<BillDetailsDTO> billDetailsDTOList = byBillingDateBetweenAndTehsil.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;
    }


    BillDetailsDTO mapToDto(Bill bill){
        double totalBill=bill.getTotalAmount();

                double penaltyRate =stationDetailsService.getPenaltyAmount(bill.getConnection().getTehsil()); // 0.2% penalty
               // double currentBill = calculateBillAmountForCustomer(bill.getConnection());
                double penaltyAmount =totalBill * penaltyRate;
                totalBill = totalBill+ penaltyAmount;

        BillDetailsDTO billDetailsDTO=new BillDetailsDTO();
        billDetailsDTO.setCustomerName(bill.getConnection().getFullName());
        billDetailsDTO.setAddress(bill.getConnection().getAddress());
        billDetailsDTO.setTehsil(bill.getConnection().getTehsil());
        billDetailsDTO.setPostalCode(bill.getConnection().getPostalCode());
        billDetailsDTO.setBillingDate(bill.getBillingDate());
        billDetailsDTO.setDueDate(bill.getDueDate());
        billDetailsDTO.setPaidAmount(bill.getPaidAmount());
        billDetailsDTO.setTotalAmount(bill.getTotalAmount());
        billDetailsDTO.setEmail(bill.getConnection().getEmail());
        billDetailsDTO.setCustomerId(bill.getConnection().getCustomerId());
        billDetailsDTO.setPaid(bill.isPaid());
        billDetailsDTO.setDueDate(bill.getDueDate());
        billDetailsDTO.setBillPaidDate(bill.getPaymentDate());
        billDetailsDTO.setAmountAfterDueDate(totalBill);
        billDetailsDTO.setDistrict(bill.getConnection().getDistrict());
        return billDetailsDTO;

    }
    Bill mapToEntity(BillDetailsDTO billDetailsDTO){
        Bill bill=new Bill();
        bill.setBillingDate(billDetailsDTO.getBillingDate());
        bill.setPaidAmount(billDetailsDTO.getPaidAmount());
        bill.setDueDate(billDetailsDTO.getDueDate());
        bill.setTotalAmount(billDetailsDTO.getTotalAmount());
        billDetailsDTO.setAddress(bill.getConnection().getAddress());
        billDetailsDTO.setTehsil(bill.getConnection().getTehsil());
        billDetailsDTO.setDistrict(bill.getConnection().getDistrict());
        billDetailsDTO.setPostalCode(bill.getConnection().getPostalCode());
        billDetailsDTO.setBillingDate(bill.getBillingDate());
        billDetailsDTO.setDueDate(bill.getDueDate());
        billDetailsDTO.setPaidAmount(bill.getPaidAmount());
        billDetailsDTO.setCustomerId(bill.getConnection().getCustomerId());
        billDetailsDTO.setPaid(bill.isPaid());
       // billDetailsDTO.setBillPaidDate(bill.getBillPaidDate());
        billDetailsDTO.setDueDate(bill.getDueDate());
        return bill;

    }
    @Override
    public List<BillDetailsDTO> getPendingBills() {
        List<Bill> unPaidBills = billRepository.findByPaidFalse();
        List<BillDetailsDTO> billDetailsDTOList =  unPaidBills.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;
    }
    @Override
    public List<BillDetailsDTO> getPaidBills() {
        List<Bill> unPaidBills = billRepository.findByPaidTrue();
        List<BillDetailsDTO> billDetailsDTOList =  unPaidBills.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;
    }

    @Override
    public List<BillDetailsDTO> findAllUnPaidBillsByCustomerId(String customerId) {
        List<Bill> paidBillsByTehsil = billRepository.findByConnection_CustomerIdAndPaidIsFalse(customerId);
        System.out.println("Size is "+paidBillsByTehsil.size());
        List<BillDetailsDTO> billDetailsDTOList = paidBillsByTehsil.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return billDetailsDTOList;
    }

}


