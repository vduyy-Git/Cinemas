package com.cinemas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.MovieResponse;
import com.cinemas.dtoresponse.WatchHistoryResponse;
import com.cinemas.dtorequest.RatingRequest;
import com.cinemas.dtorequest.WatchProgressRequest;
import com.cinemas.service.UserLibraryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/me")
@Validated
public class UserLibraryController {
    private final UserLibraryService service;
    public UserLibraryController(UserLibraryService service) { this.service = service; }

    @GetMapping("/favorites")
    public ApiResponse<List<MovieResponse>> favorites(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.success(service.favorites(userId(jwt)));
    }

    @PostMapping("/favorites/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFavorite(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        service.addFavorite(userId(jwt), movieId);
    }

    @DeleteMapping("/favorites/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId) {
        service.removeFavorite(userId(jwt), movieId);
    }

    @PutMapping("/ratings/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rate(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId,
            @Valid @RequestBody RatingRequest request) {
        service.rate(userId(jwt), movieId, request.score());
    }

    @GetMapping("/watch-history")
    public ApiResponse<List<WatchHistoryResponse>> history(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.success(service.history(userId(jwt)));
    }

    @PutMapping("/watch-history/{episodeId}")
    public ApiResponse<WatchHistoryResponse> updateProgress(@AuthenticationPrincipal Jwt jwt,
            @PathVariable Long episodeId, @Valid @RequestBody WatchProgressRequest request) {
        return ApiResponse.success(service.saveProgress(userId(jwt), episodeId, request));
    }

    private UUID userId(Jwt jwt) { return UUID.fromString(jwt.getSubject()); }
}
