package com.ElectricityAutomationInitiative.service;

import com.ElectricityAutomationInitiative.entity.FileEntity;
import com.ElectricityAutomationInitiative.payload.UserDocDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

  //  String uploadFilesForCustomer(String document, Connection connection)throws IOException;

//    void uploadFilesForCustomer1(String customerId, List<MultipartFile> files) throws IOException;
//
//    void uploadFilesForCustomer(String customerId, List<String> filePaths) throws IOException;

    FileEntity saveFile(UserDocDTO userDocDTO);
    public byte[] getDocumentsByCustomerId(String customerId);

    //String uploadFilesForCustomer(String customerId, List<MultipartFile> files);
}


