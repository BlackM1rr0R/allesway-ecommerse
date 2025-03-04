package com.example.jwt_demo.repository;

import com.example.jwt_demo.model.Favorites;
import com.example.jwt_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    Favorites findByUsers(User user);
    Optional<Favorites> findByUsers_IdAndTasks_Id(Long userId, Long taskId);


}




