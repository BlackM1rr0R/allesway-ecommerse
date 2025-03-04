package com.example.jwt_demo.service;

import com.example.jwt_demo.model.Favorites;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.model.User;
import com.example.jwt_demo.repository.FavoritesRepository;
import com.example.jwt_demo.repository.TaskRepository;
import com.example.jwt_demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void deleteFavorites(Long userId, Long taskId) {
        // Kullanıcı ve görev id'ye göre favoriyi bul
        Optional<Favorites> favorite = favoritesRepository.findByUsers_IdAndTasks_Id(userId, taskId);

        if (favorite.isPresent()) {
            Favorites favoriteToDelete = favorite.get();

            // Silinecek favori bulunduysa
            List<Task> tasks = favoriteToDelete.getTasks();

            // Favoriyi sadece bir task id ile silmeye odaklanalım
            tasks.removeIf(task -> task.getId().equals(taskId));

            // Eğer favorinin içinde başka task yoksa, favoriyi sil
            if (tasks.isEmpty()) {
                favoritesRepository.delete(favoriteToDelete);
            } else {
                // Eğer favoride hala task varsa, favoriyi güncelle
                favoriteToDelete.setTasks(tasks);
                favoritesRepository.save(favoriteToDelete);
            }
        } else {
            throw new RuntimeException("Favori bulunamadı");
        }
    }





    public void addTaskToFavorites(Long userId, Long taskId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
        Favorites favorites = favoritesRepository.findByUsers(user);
        if (favorites == null ) {
            favorites = new Favorites();
            favorites.setUsers(user);
        }
        else if(favorites.getTasks().contains(task)) {
            throw new RuntimeException("Task already exists");
        }
            favorites.addTask(task);
            favoritesRepository.save(favorites);

    }

    public List<Task> getUserFavorites(Long userId) {
        Favorites favorites = favoritesRepository.findByUsers(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        if (favorites != null) {
            return new ArrayList<>(favorites.getTasks()); // Favorilerdeki task'ları döndürüyoruz
        }
        return new ArrayList<>();
    }



}

