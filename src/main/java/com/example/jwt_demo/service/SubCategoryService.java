package com.example.jwt_demo.service;

import com.example.jwt_demo.model.SubCategory;
import com.example.jwt_demo.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryService {
    private final SubCategoryRepository subCategoriesRepository;


    public SubCategoryService(SubCategoryRepository subCategoriesRepository) {
        this.subCategoriesRepository = subCategoriesRepository;
    }
    public List<SubCategory> getSubCategoriesByCategoryId(int categoryId) {
        return subCategoriesRepository.findByCategoryId(categoryId);
    }
    public SubCategory addSubCategory(SubCategory subCategory) {
        return subCategoriesRepository.save(subCategory);
    }
}
