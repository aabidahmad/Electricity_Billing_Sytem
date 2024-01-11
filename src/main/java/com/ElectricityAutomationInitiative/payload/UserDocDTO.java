package com.ElectricityAutomationInitiative.payload;


import com.ElectricityAutomationInitiative.entity.Connection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDocDTO {

    private String fileName;
    private String fileType;
    private Connection connection;
    @Lob
    private byte[] data;

}




