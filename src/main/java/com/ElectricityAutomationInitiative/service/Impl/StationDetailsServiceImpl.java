package com.ElectricityAutomationInitiative.service.impl;

import com.ElectricityAutomationInitiative.entity.StationDetails;
import com.ElectricityAutomationInitiative.payload.StationDetailsDTO;
import com.ElectricityAutomationInitiative.repository.StationDetailsRepository;
import com.ElectricityAutomationInitiative.service.StationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StationDetailsServiceImpl implements StationDetailsService {

    private final StationDetailsRepository stationDetailsRepository;

    @Autowired
    public StationDetailsServiceImpl(StationDetailsRepository stationDetailsRepository) {
        this.stationDetailsRepository = stationDetailsRepository;
    }

    @Override
    public StationDetails getStationDetailsById(String area) {
        return stationDetailsRepository.findByStationArea(area);
    }

    @Override
    public StationDetails updateStationDetails(String stationArea, StationDetailsDTO updatedStationDetails) {
        // Find the existing StationDetails by StationArea
        StationDetails existingStationDetails = stationDetailsRepository.findByStationArea(stationArea);

        if (existingStationDetails != null) {
            // Update the fields with the provided values
            existingStationDetails.setCustomerSupportEmail(updatedStationDetails.getCustomerSupportEmail());
            existingStationDetails.setCustomerSupportNumber(updatedStationDetails.getCustomerSupportNumber());
            existingStationDetails.setMonthlyBillAmount(updatedStationDetails.getMonthlyBillAmount());
            existingStationDetails.setPenaltyAmount(updatedStationDetails.getPenaltyAmount());

            // Save and return the updated StationDetails
             stationDetailsRepository.save(existingStationDetails);
            return getStationDetailsById(existingStationDetails.getStationArea());
        } else {
            // Handle the case where StationDetails for the given StationArea is not found
            throw new RuntimeException("StationDetails not found for StationArea: " + stationArea);
        }
    }
    @Override
    public double getMonthlyBillAmount(String stationArea){
        StationDetails byStationArea = stationDetailsRepository.findByStationArea(stationArea);
        double amount = byStationArea.getMonthlyBillAmount();
        return amount;
    }


    @Override
    public double getPenaltyAmount(String stationArea) {
        StationDetails byStationArea = stationDetailsRepository.findByStationArea(stationArea);
        double penaltyAmount = byStationArea.getPenaltyAmount();
        return penaltyAmount;
    }

}
