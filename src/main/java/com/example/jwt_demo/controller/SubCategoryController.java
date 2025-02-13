package com.example.jwt_demo.controller;

import com.example.jwt_demo.model.SubCategory;
import com.example.jwt_demo.service.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/all/subCategories")
public class SubCategoryController {
    private final SubCategoryService subCategoriesService;
    public SubCategoryController(SubCategoryService subCategoriesService) {
        this.subCategoriesService = subCategoriesService;
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<List<SubCategory>> getSubCategoriesByCategoryId(@PathVariable int categoryId) {
        List<SubCategory> subCategories = subCategoriesService.getSubCategoriesByCategoryId(categoryId);
        return ResponseEntity.ok(subCategories);
    }
    @PostMapping("/add")
    public SubCategory addSubCategory(@RequestBody SubCategory subCategory) {
        return subCategoriesService.addSubCategory(subCategory);
    }
}
