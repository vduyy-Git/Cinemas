package com.cinemas.repository;

import com.cinemas.model.Favorite;
import com.cinemas.keys.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
    List<Favorite> findByUser_IdOrderByCreatedAtDesc(UUID userId);

    boolean existsByUser_IdAndMovie_Id(UUID userId, Long movieId);

    void deleteByUser_IdAndMovie_Id(UUID userId, Long movieId);
}


