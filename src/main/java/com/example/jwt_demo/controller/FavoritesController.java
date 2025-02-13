package com.example.jwt_demo.controller;
import com.example.jwt_demo.model.Favorites;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/add")
public class FavoritesController {
    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("/favorites")
    public Favorites addFavorites(@RequestBody Favorites favorites) {
        return favoritesService.addFavorites(favorites);
    }
    @GetMapping("/allFavorites")
    public List<Favorites> getAllFavorites() {
        return favoritesService.getAllFavorites();
    }
    @DeleteMapping("/delete/{Id}")
    public String deleteFavorites(@PathVariable Long Id) {
        favoritesService.deleteFavorites(Id);
        return "Silindi";
    }
    @GetMapping("/favorite/{userId}")
    public List<Task> getFavorites(@PathVariable Long userId) {
        List<Favorites> favorites = favoritesService.getFavorites(userId);

        // Her bir favori için ilgili task'ları döndürmek
        List<Task> tasks = new ArrayList<>();
        for (Favorites favorite : favorites) {
            tasks.add((Task) favorite.getTasks());  // assuming Favorites model has a 'Task' field
        }
        return tasks;
    }
    @PostMapping("/users/{userId}/favorites/{taskId}")
    public ResponseEntity<?> addTaskToFavorites(@PathVariable Long userId, @PathVariable Long taskId) {
        favoritesService.addTaskToFavorites(userId, taskId);
        return ResponseEntity.ok("Task added to favorites successfully!");
    }


}
