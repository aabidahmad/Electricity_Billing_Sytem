package com.ElectricityAutomationInitiative.service.Impl;

import com.ElectricityAutomationInitiative.entity.Complaint;
import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.excpetion.ResourceNotFound;
import com.ElectricityAutomationInitiative.payload.ConnectionDTO;
import com.ElectricityAutomationInitiative.repository.ComplaintRepository;
import com.ElectricityAutomationInitiative.repository.ConnectionRepository;
import com.ElectricityAutomationInitiative.service.ComplaintService;
import com.ElectricityAutomationInitiative.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ConnectionRepository connectionRepository;
    @Autowired
    private ConnectionService connectionService;

    @Override
    public Object logComplaint(String customerId, String complaintText) {
        System.out.println("enterd in LogComplaint");
        ConnectionDTO connectionDTO = connectionService.existsById(customerId);
        System.out.println("connection ");
        if (connectionDTO!=null) {
            // Create a new complaint
            Complaint complaint = new Complaint();
            complaint.setCustomerId(connectionDTO.getCustomerId());
            complaint.setTehsil(connectionDTO.getTehsil());
            complaint.setComplaintText(complaintText);
            complaint.setCreatedAt(new Date());
            return complaintRepository.save(complaint);
        }
           return new ResourceNotFound("not found ");
    }
    @Override
    public List<Complaint> getAllComplaints(String workplace) {
        System.out.println("exected in Complaint");

        List<Complaint> byTehsil = complaintRepository.findByTehsil(workplace);
        System.out.println(byTehsil);
        return byTehsil;
       // return null;
    }
}
