package com.ElectricityAutomationInitiative.payload;

import com.ElectricityAutomationInitiative.entity.Connection;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class ComplaintDTO {
    private String customerId;
    private String description;
    private String name;
}
