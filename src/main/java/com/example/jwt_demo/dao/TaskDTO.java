package com.example.jwt_demo.dao;

import com.example.jwt_demo.model.Task;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String price;
    private String categoryName;
    private String subCategoryName;
    private String image;

    public TaskDTO(Long id, String title, String description, String price, String s, String s1) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.categoryName = s;
        this.subCategoryName = s1;


    }

    public TaskDTO() {

    }


}
