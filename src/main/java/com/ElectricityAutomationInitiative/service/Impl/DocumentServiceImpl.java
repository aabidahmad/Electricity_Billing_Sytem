package com.ElectricityAutomationInitiative.service.Impl;



import com.ElectricityAutomationInitiative.entity.FileEntity;
import com.ElectricityAutomationInitiative.payload.UserDocDTO;
import com.ElectricityAutomationInitiative.repository.DocumentRepository;
import com.ElectricityAutomationInitiative.service.ConnectionService;
import com.ElectricityAutomationInitiative.service.DocumentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ConnectionService connectionService;
    private ModelMapper modelMapper;
    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               ConnectionService connectionService,
                               ModelMapper modelMapper ) {
        this.documentRepository = documentRepository;
        this.connectionService=connectionService;
        this.modelMapper=modelMapper;
    }
//    @Override
//    public void uploadFilesForCustomer1(String customerId, List<MultipartFile> files) throws IOException {
//        for (MultipartFile file : files) {
//            // Perform any necessary processing, validation, and saving logic here
//            // Save each file and associate it with the given customer ID
//            UserDoc userDoc = new UserDoc();
//            userDoc.setName(file.getOriginalFilename());
//            userDoc.setType(file.getContentType());
//            userDoc.setFileData(file.getBytes());
//
//            // Associate the UserDoc entity with the given customer ID (if needed)
//            // Assuming you have a Connection entity with the customerId field
//            Connection connection = connectionService.getConnectionByConnectionId(customerId);
//            userDoc.setConnection(connection);
//
//            // Save the UserDoc entity to the database
//            documentRepository.save(userDoc);
//        }
//    }
//    public void uploadFilesForCustomer(String customerId, List<String> filePaths) throws IOException {
//        for (String filePath : filePaths) {
//            // Load and process each file using the file path
//            File file = new File(filePath);
//
//            // Perform any necessary processing, validation, and saving logic here
//            // Save each file and associate it with the given customer ID
//            UserDoc userDoc = new UserDoc();
//            userDoc.setName(file.getName());
//            userDoc.setType("application/pdf"); // Set the appropriate content type
//            userDoc.setFileData(Files.readAllBytes(file.toPath()));
//
//            // Associate the UserDoc entity with the given customer ID (if needed)
//            // Assuming you have a Connection entity with the customerId field
//            Connection connection = connectionService.getConnectionByConnectionId(customerId);
//            userDoc.setConnection(connection);
//
//            // Save the UserDoc entity to the database
//            documentRepository.save(userDoc);
//        }
//    }
public byte[] getDocumentsByCustomerId(String customerId) {
    System.out.println(" Entered ");
    byte [] file = documentRepository.findDataByCustomerId(customerId);
    if (file == null) {
        System.out.println("FileEntity NUll ");
    }
    return file;
}

    @Override
    public FileEntity saveFile(UserDocDTO userDocDTO) {

        FileEntity fileEntity=new FileEntity();
        fileEntity.setConnection(userDocDTO.getConnection());
        fileEntity.setFileName(userDocDTO.getFileName());
        fileEntity.setFileType(userDocDTO.getFileType());
        fileEntity.setData(userDocDTO.getData());
        fileEntity.setConnection(userDocDTO.getConnection());

        return documentRepository.save(fileEntity);

        }


    private FileEntity mapToEntity(UserDocDTO userDocDTO){
        FileEntity  fileEntity = modelMapper.map(userDocDTO,FileEntity.class);
        return  fileEntity;
    }
    private UserDocDTO mapTODTO(FileEntity fileEntity){
        UserDocDTO userDocDTO=modelMapper.map(fileEntity,UserDocDTO.class);
        return userDocDTO;
    }

    }
