package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.entity.StationDetails;
import com.ElectricityAutomationInitiative.payload.StationDetailsDTO;
import com.ElectricityAutomationInitiative.service.StationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/station-details")
@CrossOrigin(origins = "http://localhost:4200")
public class StationDetailsController {

    private final StationDetailsService stationDetailsService;

    @Autowired
    public StationDetailsController(StationDetailsService stationDetailsService) {
        this.stationDetailsService = stationDetailsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<StationDetails> getStationDetails( @RequestParam("workplace") String workplace) {
        System.out.println(workplace);
        StationDetails stationDetails = stationDetailsService.getStationDetailsById(workplace);
        return ResponseEntity.ok(stationDetails);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateStationDetails(
            @RequestParam("workplace") String workplace,
            @RequestBody StationDetailsDTO stationDetailsDTO
            ) {
        StationDetails updatedDetails = stationDetailsService.updateStationDetails(workplace, stationDetailsDTO);
        if (updatedDetails != null) {
            return ResponseEntity.ok(updatedDetails);
        } else {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
