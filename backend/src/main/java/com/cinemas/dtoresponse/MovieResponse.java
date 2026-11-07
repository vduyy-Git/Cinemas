package com.cinemas.dtoresponse;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import com.cinemas.model.Movie;

public record MovieResponse(
        Long id, String type, String title, String originalTitle, String slug, String description,
        String posterUrl, String backdropUrl, String trailerUrl, Integer releaseYear,
        Integer durationMinutes, String ageRating, Integer countryId, String countryName,
        String status, Long viewCount, BigDecimal ratingAvg, Integer ratingCount,
        OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    public static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getType().name(), movie.getTitle(),
                movie.getOriginalTitle(), movie.getSlug(), movie.getDescription(), movie.getPosterUrl(),
                movie.getBackdropUrl(), movie.getTrailerUrl(), movie.getReleaseYear(), movie.getDurationMinutes(),
                movie.getAgeRating(), movie.getCountry() == null ? null : movie.getCountry().getId(),
                movie.getCountry() == null ? null : movie.getCountry().getName(), movie.getStatus().name(),
                movie.getViewCount(), movie.getRatingAvg(), movie.getRatingCount(), movie.getCreatedAt(),
                movie.getUpdatedAt());
    }
}
