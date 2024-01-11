package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.Complaint;

import java.util.List;


public interface ComplaintService {
    Object logComplaint(String customerId, String complaintText);

    List<Complaint> getAllComplaints(String workplace);
}
