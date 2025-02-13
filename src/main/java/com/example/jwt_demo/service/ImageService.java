package com.example.jwt_demo.service;

import com.example.jwt_demo.imageutil.ImageUtils;
import com.example.jwt_demo.model.Image;
import com.example.jwt_demo.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public String uploadImage(MultipartFile imageFile) throws IOException {
        Image imageToSave = Image.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .build();
        imageRepository.save(imageToSave);
        return "Image uploaded successfully: " + imageFile.getOriginalFilename();
    }

    public byte[] downloadImage(String imageName) {
        Optional<Image> dbImage = imageRepository.findByName(imageName);

        return dbImage.map(image -> {
            try {
                return ImageUtils.decompressImage(image.getImageData());
            } catch (DataFormatException | IOException e) {
                throw new RuntimeException("Error decompressing image: " + imageName, e);
            }
        }).orElseThrow(() -> new RuntimeException("Image not found: " + imageName));
    }

    public ResponseEntity<List<Image>> getAllImages() {
        return ResponseEntity.ok(imageRepository.findAll());
    }
}
