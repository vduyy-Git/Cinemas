package com.cinemas.repository;

import com.cinemas.model.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUser_IdOrderByLastWatchedAtDesc(UUID userId);

    Optional<WatchHistory> findByUser_IdAndEpisode_Id(UUID userId, Long episodeId);
}


