package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findAllImagesById(Long id);

    List<Image> findByName(String name);
}
