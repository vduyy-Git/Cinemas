package com.cinemas.repository;

import com.cinemas.model.VideoSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoSourceRepository extends JpaRepository<VideoSource, Long> {
    List<VideoSource> findByEpisode_IdAndActiveTrue(Long episodeId);
}


