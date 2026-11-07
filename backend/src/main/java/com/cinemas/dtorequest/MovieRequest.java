package com.cinemas.dtorequest;

import com.cinemas.enums.MovieStatus;
import com.cinemas.enums.MovieType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MovieRequest(
        @NotNull MovieType type,
        @NotBlank @Size(max = 255) String title,
        @Size(max = 255) String originalTitle,
        @NotBlank @Size(max = 255) @Pattern(regexp = "[a-z0-9]+(?:-[a-z0-9]+)*") String slug,
        String description,
        @Size(max = 2048) String posterUrl,
        @Size(max = 2048) String backdropUrl,
        @Size(max = 2048) String trailerUrl,
        @Min(1888) @Max(2200) Integer releaseYear,
        @Min(1) Integer durationMinutes,
        @Size(max = 32) String ageRating,
        Integer countryId,
        @NotNull MovieStatus status) {
}
