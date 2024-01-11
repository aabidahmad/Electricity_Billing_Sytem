package com.ElectricityAutomationInitiative.repository;

import com.ElectricityAutomationInitiative.controller.StationDetailsController;
import com.ElectricityAutomationInitiative.entity.StationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationDetailsRepository extends JpaRepository<StationDetails,Long> {
    StationDetails findByStationArea(String area);
    Double findMonthlyBillAmountByStationArea(String stationArea);

    Double findPenaltyAmountByStationArea(String stationArea);

}
