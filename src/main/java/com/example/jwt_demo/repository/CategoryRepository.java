package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {


    Category findByCategoryName(String categoryName);
}

