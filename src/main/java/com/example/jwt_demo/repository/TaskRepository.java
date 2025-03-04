package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByCategory_CategoryName(String categoryName);

    List<Task> findBySubCategory_SubCategoryName(String subCategoryName);

    List<Task> findByTitleContainingIgnoreCase(String taskName);
}
