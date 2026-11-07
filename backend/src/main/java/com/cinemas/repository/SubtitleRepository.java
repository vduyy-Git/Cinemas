package com.cinemas.repository;

import com.cinemas.model.Subtitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtitleRepository extends JpaRepository<Subtitle, Long> {
    List<Subtitle> findByEpisode_Id(Long episodeId);
}


