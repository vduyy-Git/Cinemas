package com.cinemas.repository;

import com.cinemas.model.Rating;
import com.cinemas.keys.RatingId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, RatingId> {
    boolean existsByUser_IdAndMovie_Id(UUID userId, Long movieId);
}


