package com.ElectricityAutomationInitiative.controller;

import com.ElectricityAutomationInitiative.entity.Connection;
import com.ElectricityAutomationInitiative.entity.FileEntity;
import com.ElectricityAutomationInitiative.payload.ConnectionDTO;
import com.ElectricityAutomationInitiative.payload.UserDocDTO;
import com.ElectricityAutomationInitiative.service.ConnectionService;
import com.ElectricityAutomationInitiative.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@CrossOrigin(origins= "http://localhost:4200")
@RequestMapping("/api/documents")
public class DocumentController {
    private final DocumentService documentService;
    private final ConnectionService connectionService;
    @Autowired
    public DocumentController(DocumentService documentService, ConnectionService connectionService) {
        this.documentService = documentService;
        this.connectionService = connectionService;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,@RequestParam("customerId")String customerId) {
        System.out.println("executed ");
        Connection connection = connectionService.getConnectionByCustomerId(customerId);
        if (file.isEmpty()) {
            System.out.println("empty");
            return ResponseEntity.internalServerError().body("Please select a file to upload.");
        }
        if(connection==null){
            return ResponseEntity.badRequest().body("Connection data in not available");
        }

        UserDocDTO userDocDTO=new UserDocDTO();
        userDocDTO.setFileName(file.getOriginalFilename());
        userDocDTO.setFileType(file.getContentType());
         userDocDTO.setConnection(connection);
        try {
            System.out.println("try");
            userDocDTO.setData(file.getBytes());
        } catch (IOException e) {
            // throw new RuntimeException(e);
            System.out.println("Exception");
            e.printStackTrace();
        }

        FileEntity fileEntity = documentService.saveFile(userDocDTO);
        // Example of sending a JSON response in Spring Boot
        return ResponseEntity.ok().body("{\"message\": \"File uploaded successfully\"}");
        //return ResponseEntity.ok("File uploaded successfully with ID: " + savedFile.getId());
    }

}