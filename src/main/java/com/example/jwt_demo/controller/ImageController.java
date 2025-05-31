package com.example.jwt_demo.controller;

import com.example.jwt_demo.model.Image;
import com.example.jwt_demo.model.ImageUploadResponse;
import com.example.jwt_demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService imageService;
    @PostMapping
    public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] files) throws IOException {
        List<ImageUploadResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            ImageUploadResponse response = imageService.uploadImage(file);
            responses.add(response);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(responses);
    }
    @GetMapping("/info/{name}")
    public ResponseEntity<List<Image>> getImageInfoByName(@PathVariable("name") String name) {
        List<Image> images = imageService.getInfoByImageByName(name);
        return ResponseEntity.ok(images);
    }
    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload";
    }

    @GetMapping("/{name}")
    public ResponseEntity<byte[]> getImageByName(@PathVariable("name") String name) {
        List<Image> images = imageService.getInfoByImageByName(name);
        if (images.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Image imageData = images.get(0); // İlk resmi al
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imageData.getType()))
                .body(imageData.getImageData());
    }
}
