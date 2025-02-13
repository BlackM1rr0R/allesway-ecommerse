package com.example.jwt_demo.dao;

import com.example.jwt_demo.model.Task;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String price;
    private String categoryName;
    private String subCategoryName;


}
