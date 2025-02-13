package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.Category;
import com.example.jwt_demo.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategoryId(int categoryId);
    List<SubCategory> findBySubCategoryNameAndCategory(String subCategoryName, Category category);

}
