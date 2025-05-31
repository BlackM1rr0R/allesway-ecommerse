package com.example.jwt_demo.service;

import com.example.jwt_demo.dao.TaskDTO;
import com.example.jwt_demo.model.*;
import com.example.jwt_demo.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoriesRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoriesRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoriesRepository = subCategoriesRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public void deleteTaskById(Long taskId, Long userId) {
        User user = userRepository.findById(userId).get();
        Task task = taskRepository.findById(taskId).get();
        if (!task.getUsers().getId().equals(user.getId())) {
            throw new RuntimeException("You are not allowed to delete this task");
        }
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(Long userId, Long taskId, Task taskDTO) {
        User user = userRepository.findById(userId).get();
        Task taskToUpdate = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        if (!taskToUpdate.getUsers().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to update this task!");
        }
        taskToUpdate.setTitle(taskDTO.getTitle());
        taskToUpdate.setDescription(taskDTO.getDescription());
        taskToUpdate.setPrice(taskDTO.getPrice());

        return taskRepository.save(taskToUpdate);
    }


    public List<Task> getTasksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getTasks();
    }


    public List<TaskDTO> getTaskByCategory(String categoryName) {
        List<Task> tasks = taskRepository.findByCategory_CategoryName(categoryName);
        List<TaskDTO> taskDTOs = new ArrayList<>();
        for (Task task : tasks) {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setTitle(task.getTitle());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setPrice(task.getPrice());
            if (task.getCategory() != null) {
                taskDTO.setCategoryName(task.getCategory().getCategoryName());

            }
            if (task.getSubCategory() != null) {
                taskDTO.setSubCategoryName(task.getSubCategory().getSubCategoryName());
            } else {
                taskDTO.setSubCategoryName("Alt Kategori Yok");
            }
            taskDTOs.add(taskDTO);
        }
        return taskDTOs;
    }

    public List<TaskDTO> getTaskBySubCategoryName(String subCategoryName) {
        List<Task> tasks = taskRepository.findBySubCategory_SubCategoryName(subCategoryName);
        return tasks.stream().map(task -> {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setId(task.getId());
            taskDTO.setTitle(task.getTitle());
            taskDTO.setDescription(task.getDescription());
            taskDTO.setPrice(task.getPrice());
            if (task.getCategory() != null) {
                taskDTO.setCategoryName(task.getCategory().getCategoryName());
            }
            if (task.getSubCategory() != null) {
                taskDTO.setSubCategoryName(task.getSubCategory().getSubCategoryName());
            }
            return taskDTO;
        }).collect(Collectors.toList());
    }

//    public TaskDTO getTaskById(Long taskId) {
//        Task task = taskRepository.findById(taskId)
//                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
//        TaskDTO taskDTO = new TaskDTO();
//        taskDTO.setId(task.getId());
//        taskDTO.setTitle(task.getTitle());
//        taskDTO.setDescription(task.getDescription());
//        taskDTO.setPrice(task.getPrice());
//        if (task.getCategory() != null) {
//            taskDTO.setCategoryName(task.getCategory().getCategoryName());
//        } else {
//            taskDTO.setCategoryName("Kategori Yok");
//        }
//        if (task.getSubCategory() != null) {
//            taskDTO.setSubCategoryName(task.getSubCategory().getSubCategoryName());
//        } else {
//            taskDTO.setSubCategoryName("Alt Kategori Yok");
//        }
//        return taskDTO;
//    }
    public List<Task> getTaskByTaskName(String taskName) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(taskName);
    return tasks.stream()
            .limit(7)
            .collect(Collectors.toList());
    }

//    public Task createTask(Task task) {
//
//        task.setTitle(task.getTitle());
//        task.setDescription(task.getDescription());
//        task.setPrice(task.getPrice());
//        Category category = task.getCategory();
//        if (category != null) {
//            category.setCategoryName(task.getCategory().getCategoryName());
//        }
//        SubCategory subCategory = task.getSubCategory();
//        if (subCategory != null) {
//            subCategory.setSubCategoryName(task.getSubCategory().getSubCategoryName());
//        }
//        return taskRepository.save(task);
//    }
public Task createTask(Task task) {
    return taskRepository.save(task);
}

    public byte[] getTaskImage(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        return ImageUtil.decompressImage(task.getImage());
    }


    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> {
            TaskDTO taskDTO = new TaskDTO(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getPrice(),
                    task.getCategory() != null ? task.getCategory().getCategoryName() : "Kategori yok",  // Kategori adı
                    task.getSubCategory() != null ? task.getSubCategory().getSubCategoryName() : "Alt kategori yok"  // Alt kategori adı
            );

            // Resim var mı kontrolü ve Base64 formatına çevirme
            if (task.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(task.getImage());
                taskDTO.setImage("data:image/jpeg;base64," + base64Image);  // Base64 formatında resim
            }

            return taskDTO;
        }).collect(Collectors.toList());
    }


    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).get();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setPrice(task.getPrice());
        if (task.getCategory() != null) {
            taskDTO.setCategoryName(task.getCategory().getCategoryName());

        }
        if (task.getSubCategory() != null) {
            taskDTO.setSubCategoryName(task.getSubCategory().getSubCategoryName());
        }
        return taskDTO;
    }
}
