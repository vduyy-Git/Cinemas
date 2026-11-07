package com.cinemas.dtoresponse;

import com.cinemas.model.Episode;

public record EpisodeResponse(Long id, Long movieId, Long seasonId, Integer episodeNumber, String title,
        String description, String thumbnailUrl, Integer durationMinutes) {
    public static EpisodeResponse from(Episode value) {
        return new EpisodeResponse(value.getId(), value.getMovie().getId(),
                value.getSeason() == null ? null : value.getSeason().getId(), value.getEpisodeNumber(),
                value.getTitle(), value.getDescription(), value.getThumbnailUrl(), value.getDurationMinutes());
    }
}
