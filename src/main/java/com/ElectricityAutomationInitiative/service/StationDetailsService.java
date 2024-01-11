package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.StationDetails;
import com.ElectricityAutomationInitiative.payload.StationDetailsDTO;

public interface StationDetailsService {
    StationDetails getStationDetailsById(String area);



    StationDetails updateStationDetails(String stationArea, StationDetailsDTO updatedStationDetails);

    double getMonthlyBillAmount(String stationArea);
    double getPenaltyAmount(String stationArea);
}
