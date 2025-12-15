
package com.laptops.ALaptop.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequestMapping("/api")
public class MainRestController {

    @Value("${image.base.path}")
    private String imageBasePath;
    
    @Value("${image.encryption.key:defaultkey123456}")
    private String encryptionKey;

    @GetMapping("/{accountId}/{imageName:.+}")
    public ResponseEntity<byte[]> getImageByAccount(
            @PathVariable long accountId,
            @PathVariable String imageName
    ) {
        return getImage(accountId, imageName);
    }
    
    @GetMapping("/laptop-shop/info/{imageName:.+}")
    public ResponseEntity<byte[]> getImageForInfo(
            @PathVariable String imageName
    ) {
        // Extract account ID from image name if it contains user_ prefix
        if (imageName.startsWith("user_")) {
            String[] parts = imageName.split("/", 2);
            if (parts.length == 2) {
                long accountId = Long.parseLong(parts[0].substring(5)); // Remove "user_" prefix
                return getImage(accountId, parts[1]);
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image format");
    }
    
    private ResponseEntity<byte[]> getImage(long accountId, String imageName) {

        if (imageName.contains("..") || imageName.contains("%2e") || imageName.contains("%2f")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image name");
        }

        try {
            Path basePath = Paths.get(imageBasePath, "user_" + accountId).normalize();
            Path imagePath = basePath.resolve(imageName).normalize();
            
            if (!imagePath.startsWith(basePath)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path");
            }

            if (!Files.exists(imagePath)) {
                String encryptedName = imageName + ".enc";
                imagePath = basePath.resolve(encryptedName).normalize();
                
                if (!imagePath.startsWith(basePath) || !Files.exists(imagePath)) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
                }
                
                byte[] encryptedBytes = Files.readAllBytes(imagePath);
                byte[] imageBytes = decrypt(encryptedBytes);
                String contentType = getContentTypeFromName(imageName);
                
                return ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(imageBytes);
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = getContentTypeFromName(imageName);
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to read image",
                    e
            );
        }
    }
    
    private byte[] decrypt(byte[] encryptedData) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptedData);
    }
    
    private String getContentTypeFromName(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerName.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (lowerName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        }
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
}
