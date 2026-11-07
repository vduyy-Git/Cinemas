package com.cinemas.repository;

import com.cinemas.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findByMovie_IdOrderByEpisodeNumberAsc(Long movieId);

    List<Episode> findBySeason_IdOrderByEpisodeNumberAsc(Long seasonId);
}


