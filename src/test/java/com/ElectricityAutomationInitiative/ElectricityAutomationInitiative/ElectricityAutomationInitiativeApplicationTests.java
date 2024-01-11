package com.ElectricityAutomationInitiative.ElectricityAutomationInitiative;


import com.ElectricityAutomationInitiative.controller.AdminRegistrationController;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.repository.BillRepository;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@AutoConfigureMockMvc
@SpringBootTest
class ElectricityAutomationInitiativeApplicationTests {


    @Autowired
	BillService billService;
	@Autowired
	AdminRegistrationController adminRegistrationController;
	@Autowired
	PaymentTransactionService paymentTransactionService;

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired
		AdminService adminService;
	@Autowired
	ConnectionRepository connectionRepository;
	@Autowired
	BillRepository billRepository;
	@Autowired
	ConnectionService connectionService;
	@Test
	public void testGenerateBillForCustomersOn20th() {
		billService.generateBillForCustomersOn20th();
	//	 billService.findPaidBillsByTehsil("kup");
		// paymentTransactionService.createStripeCustomer("SDEW435DSFER","adil ","email1@gmail.com");
    //    connectionService.resetPassword("K0NAW1QY5S82","123bbb");
		//adminService.resetPassword("EMP881222231","123qaz");
//		Connection connection = connectionRepository.findByEmailOrCustomerId("K0NAW1QY5S82", "K0NAW1QY5S82");
//		System.out.println(connection);
		//	billService.getAllPaidBills(true);
	  // billService.getBillDetailsByCustomerId("LLL5SOZUZUFJ");
	}
	@Test
	public void test() {

		userDetailsService.loadUserByUsername("EMP881222927");

	}

	@Autowired
	private MockMvc mockMvc;
//	@Test
//
//	public void testFindPaidBillsByTehsil() throws Exception {
//		mockMvc.perform(get("/api/paid-bills")
//						.param("tehsil", "kup")
//						.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				// Add additional assertions as needed
//				.andReturn();
//	}
//	public List<Object[]> calculateTotalPaidAmountByDistrict() {
//		return billRepository.calculateTotalPaidAmountByDistrict();
//	}
//
//	@Test
//	public void testCalculateTotalPaidAmountByDistrict() {
//		List<Object[]> result =billRepository.calculateTotalPaidAmountByDistrict();
//
//		// Create a Map to store the results
//		Map<String, Double> districtPaidAmountMap = new HashMap<>();
//
//		// Populate the map with the results
//		for (Object[] row : result) {
//			String district = (String) row[0];
//			Double totalPaidAmount = (Double) row[1];
//			districtPaidAmountMap.put(district, totalPaidAmount);
//		}
//
//		// Print the map
//		districtPaidAmountMap.forEach((district, paidAmount) -> {
//			System.out.println("District: " + district + ", Total Paid Amount: " + paidAmount);
//		});
//	}
	@Test
	public void calculateYearlyRevenue() {
		List<Object[]> results = billRepository.calculateTotalPaidAmountByYearAndMonth();

		Map<String, Double> yearlyRevenueMap = new HashMap<>();

		for (Object[] result : results) {
			int year = (int) result[0];
			int month = (int) result[1];
			double totalPaidAmount = (double) result[2];

			String monthYear = String.format("%02d-%04d", month, year);
			yearlyRevenueMap.put(monthYear, yearlyRevenueMap.getOrDefault(monthYear, 0.0) + totalPaidAmount);
		}

		yearlyRevenueMap.forEach((monthYear, totalPaidAmount) -> {
			System.out.println(monthYear + " : " + totalPaidAmount);
		});
	}
	@Test
	public void calculateUnpaidRevenueByTehsil() {
		List<Object[]> objects = billRepository.calculateUnpaidRevenueByDistrict();
		Map<String, Double> unpaidRevenueByTehsil = new HashMap<>();
		for (Object[] result : objects) {
			String tehsil = (String) result[0];
			Double totalPaidAmount = (Double) result[1];
			unpaidRevenueByTehsil.put(tehsil, totalPaidAmount);
		}
		unpaidRevenueByTehsil.forEach((tehsil, totalPaidAmount) -> {
			System.out.println("District "+tehsil + " : " + totalPaidAmount);
		});


	}
	@Test
	void printUnpaidRevenueByMonthAndYear() {
		List<Map<String, Object>> unpaidRevenue = billRepository.findUnpaidRevenueGroupedByMonthAndYear();

		if (unpaidRevenue != null) {
			unpaidRevenue.forEach(item -> {
				Object year = item.get("year");
				Object month = item.get("month");
				Object totalAmount = item.get("totalAmount");

				if (year != null && month != null && totalAmount != null) {
					System.out.println("Year: " + year + ", Month: " + month + ", Total Amount: " + totalAmount);
				} else {
					System.out.println("Invalid data in the list: " + item);
				}
			});
		} else {
			System.out.println("No unpaid revenue data found.");
		}
	}
	@Test
	public void printBillCountByDistrict() {
		List<Object[]> billCounts = billRepository.getBillCountByDistrict();

		for (Object[] result : billCounts) {
			String district = (String) result[0];
			Long numberOfBills = (Long) result[1];
			System.out.println(district + ":" + numberOfBills);
		}
	}

}

