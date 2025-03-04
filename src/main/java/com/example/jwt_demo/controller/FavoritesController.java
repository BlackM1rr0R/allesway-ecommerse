package com.example.jwt_demo.controller;
import com.example.jwt_demo.model.Favorites;
import com.example.jwt_demo.model.Task;
import com.example.jwt_demo.service.FavoritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @DeleteMapping("/delete/{userId}/{taskId}")
    public String deleteFavorites(@PathVariable Long userId, @PathVariable Long taskId) {
        favoritesService.deleteFavorites(userId, taskId);
        return "Silindi";
    }


    @PostMapping("/users/{userId}/favorites/{taskId}")
    public ResponseEntity<?> addTaskToFavorites(@PathVariable Long userId, @PathVariable Long taskId) {
        favoritesService.addTaskToFavorites(userId, taskId);
        return ResponseEntity.ok("Task added to favorites successfully!");
    }

    @GetMapping("/users/{userId}/favorites")
    public ResponseEntity<List<Task>> getUserFavorites(@PathVariable Long userId) {
        List<Task> userFavorites = favoritesService.getUserFavorites(userId);
        return ResponseEntity.ok(userFavorites);
    }

}
