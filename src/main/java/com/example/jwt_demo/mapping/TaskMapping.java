package com.example.jwt_demo.mapping;

import com.example.jwt_demo.dao.TaskDTO;
import com.example.jwt_demo.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapping {
    TaskDTO taskToTaskDTO(Task task);
    Task taskDTOToTask(TaskDTO taskDTO);
}

