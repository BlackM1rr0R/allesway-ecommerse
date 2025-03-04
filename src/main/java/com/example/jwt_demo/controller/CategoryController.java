package com.example.jwt_demo.controller;

import com.example.jwt_demo.model.Category;
import com.example.jwt_demo.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/all/category")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @PreAuthorize("hasAuthority('USER,ADMIN')")
    @GetMapping("/get/all/category")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }
    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }
}
