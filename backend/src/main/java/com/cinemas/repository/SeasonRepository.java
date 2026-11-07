package com.cinemas.repository;

import com.cinemas.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findByMovie_IdOrderBySeasonNumberAsc(Long movieId);

    Optional<Season> findByMovie_IdAndSeasonNumber(Long movieId, Integer seasonNumber);
}


