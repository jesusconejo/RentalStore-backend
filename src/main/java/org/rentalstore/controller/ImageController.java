package org.rentalstore.controller;

import org.apache.logging.log4j.LogManager;
import org.rentalstore.service.imp.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api-images")
@CrossOrigin(origins = "http://localhost:5173/")
public class ImageController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
    private final S3Service s3Service;
    @Autowired
    public ImageController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = s3Service.uploadFile(file);
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(500).body("Error al subir la imagen");
        }
    }
}

