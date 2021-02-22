package com.game.b1ingservice.service;

import com.game.b1ingservice.commons.Constants;
import com.game.b1ingservice.config.FileStorageProperties;
import com.game.b1ingservice.exception.ErrorMessageException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new ErrorMessageException(Constants.ERROR.ERR_99001);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name

        String fileName = StringUtils.cleanPath(UUID.randomUUID().toString()+"."+ FilenameUtils.getExtension(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new ErrorMessageException(Constants.ERROR.ERR_99002);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new ErrorMessageException(Constants.ERROR.ERR_99001);
        }
    }
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new ErrorMessageException(Constants.ERROR.ERR_99003);
            }
        } catch (MalformedURLException ex) {
            throw new ErrorMessageException(Constants.ERROR.ERR_99003);
        }
    }
}
