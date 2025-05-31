package com.example.jwt_demo.service;

import com.example.jwt_demo.model.Image;
import com.example.jwt_demo.model.ImageUploadResponse;
import com.example.jwt_demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;
    public ImageUploadResponse uploadImage(MultipartFile file) throws IOException {
        Image imageData = new Image.Builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtil.compressImage(file.getBytes()))
                .build();

        imageRepository.save(imageData);
        return new ImageUploadResponse("Image uploaded successfully: " + file.getOriginalFilename());
    }

    public List<Image> getInfoByImageByName(String name) {
        List<Image> images = imageRepository.findByName(name);
        return images.stream()
                .map(image -> new Image.Builder()
                        .name(image.getName())
                        .type(image.getType())
                        .imageData(ImageUtil.decompressImage(image.getImageData()))
                        .build())
                .toList();
    }

    public byte[] getImage(String name) {
        List<Image> images = imageRepository.findByName(name);
        if (images.isEmpty()) {
            return null;
        }
        return ImageUtil.decompressImage(images.get(0).getImageData());
    }
}
