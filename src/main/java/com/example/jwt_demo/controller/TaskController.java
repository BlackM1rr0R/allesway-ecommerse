package com.example.jwt_demo.controller;

import com.example.jwt_demo.dao.TaskDTO;
import com.example.jwt_demo.model.Category;
import com.example.jwt_demo.model.SubCategory;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.model.User;
import com.example.jwt_demo.repository.CategoryRepository;
import com.example.jwt_demo.repository.SubCategoryRepository;
import com.example.jwt_demo.repository.UserRepository;
import com.example.jwt_demo.service.FavoritesService;
import com.example.jwt_demo.service.ImageUtil;
import com.example.jwt_demo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final FavoritesService favoritesService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    public TaskController(TaskService taskService, FavoritesService favoritesService, UserRepository userRepository, CategoryRepository categoryRepository, SubCategoryRepository subCategoryRepository) {
        this.taskService = taskService;
        this.favoritesService = favoritesService;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
    }
    @PostMapping(value = "/createTask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Task> createTask(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("subCategoryName") String subCategoryName,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setPrice(String.valueOf(price));
        Category category = categoryRepository.findByCategoryName(categoryName);
        if (category == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // "Kategori bulunamadı"
        }

        SubCategory subCategory = subCategoryRepository.findBySubCategoryName(subCategoryName);
        if (subCategory == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // "Alt kategori bulunamadı"
        }

        if (subCategory.getCategory().getId() != category.getId()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); // "Alt kategori, bu kategoriye ait değil"
        }
        task.setCategory(category);
        task.setSubCategory(subCategory);

        if (image != null && !image.isEmpty()) {
            byte[] compressedImage = ImageUtil.compressImage(image.getBytes());
            task.setImage(compressedImage);
        }
        task.setUsers(user);
        Task savedTask = taskService.createTask(task);
        return ResponseEntity.ok(savedTask);
    }


    @GetMapping("/task/{id}/image")
    public ResponseEntity<String> getTaskImage(@PathVariable Long id) {
        byte[] imageData = taskService.getTaskImage(id);  // Resmin verisini al
        String base64Image = Base64.getEncoder().encodeToString(imageData);  // Base64 formatına çevir
        return ResponseEntity.ok().body("data:image/jpeg;base64," + base64Image);  // Base64 verisini döndür
    }


    @GetMapping("/allTask")
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/get/{userId}")
    public List<Task> getTasksByUserId(@PathVariable Long userId) {
        return taskService.getTasksByUserId(userId);
    }


    @DeleteMapping("/remove/{userId}/{taskId}")
    public void deleteTaskById(@PathVariable Long taskId, @PathVariable Long userId) {
        taskService.deleteTaskById(taskId, userId);
    }

    @PutMapping("/updateTask/{userId}/{taskId}")
    public Task updateTask(@PathVariable Long userId, @PathVariable Long taskId, @RequestBody Task taskDTO) {
        return taskService.updateTask(userId, taskId, taskDTO);
    }

   @GetMapping("/details/{taskId}")
   public TaskDTO getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
   }

    @GetMapping("/task/categories/{categoryName}")
    public List<TaskDTO> getTasksByCategory(@PathVariable String categoryName) {
        return taskService.getTaskByCategory(categoryName);
    }

    @GetMapping("/categories/sub/{subCategoryName}")
    public List<TaskDTO> getTaskBySubCategoryName(@PathVariable String subCategoryName) {
        return taskService.getTaskBySubCategoryName(subCategoryName);
    }

    @GetMapping("/search/{taskName}")
    public List<Task> getTasksByTaskName(@PathVariable String taskName) {
        return taskService.getTaskByTaskName(taskName);
    }
}
