package com.example.jwt_demo.service;

import com.example.jwt_demo.model.Favorites;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.model.User;
import com.example.jwt_demo.repository.FavoritesRepository;
import com.example.jwt_demo.repository.TaskRepository;
import com.example.jwt_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class FavoritesService {
    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public FavoritesService(FavoritesRepository favoritesRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.favoritesRepository = favoritesRepository;

        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public Favorites addFavorites(@RequestBody Favorites favorites) {
        return favoritesRepository.save(favorites);
    }

    public List<Favorites> getAllFavorites() {
        return favoritesRepository.findAll();
    }


    public void deleteFavorites(Long id) {
        favoritesRepository.deleteById(id);
    }

    public void addTaskToFavorites(Long userId, Long taskId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));


        Favorites favorites = favoritesRepository.findByUsers(user);
        if (favorites == null) {
            favorites = new Favorites();
            favorites.setUsers(user);
        }


            favorites.addTask(task);
            favoritesRepository.save(favorites);

    }


    public List<Favorites> getFavorites(Long userId) {
        return favoritesRepository.findByUsersId(userId);
    }

}

