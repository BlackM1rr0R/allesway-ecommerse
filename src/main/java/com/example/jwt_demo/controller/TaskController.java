package com.example.jwt_demo.controller;

import com.example.jwt_demo.dao.TaskDTO;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.model.User;
import com.example.jwt_demo.repository.UserRepository;
import com.example.jwt_demo.service.FavoritesService;
import com.example.jwt_demo.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final FavoritesService favoritesService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, FavoritesService favoritesService, UserRepository userRepository) {
        this.taskService = taskService;
        this.favoritesService = favoritesService;
        this.userRepository = userRepository;
    }
    @PostMapping("/createTask")
    public Task createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Kullanıcı oturumu yok");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + username);
        }
        task.setUsers(user);
        return taskService.createTask(task);
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
