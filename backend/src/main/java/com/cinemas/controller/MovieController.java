package com.cinemas.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.MovieResponse;
import com.cinemas.dtorequest.MovieRequest;
import com.cinemas.enums.MovieType;
import com.cinemas.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/movies")
@Validated
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) { this.service = service; }

    @GetMapping
    public ApiResponse<Page<MovieResponse>> browse(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) MovieType type,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer countryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.browse(q, type, year, countryId, page, size));
    }

    @GetMapping("/{slug}")
    public ApiResponse<MovieResponse> get(@PathVariable String slug) {
        return ApiResponse.success(service.getPublished(slug));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('admin','moderator')")
    public ApiResponse<MovieResponse> create(@Valid @RequestBody MovieRequest request) {
        return ApiResponse.success("Movie created", service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin','moderator')")
    public ApiResponse<MovieResponse> update(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
        return ApiResponse.success("Movie updated", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('admin')")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
