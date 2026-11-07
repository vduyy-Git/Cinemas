package com.cinemas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.model.Country;
import com.cinemas.model.Genre;
import com.cinemas.repository.CountryRepository;
import com.cinemas.repository.GenreRepository;

@RestController
@RequestMapping("/")
public class CatalogController {
    private final GenreRepository genres;
    private final CountryRepository countries;

    public CatalogController(GenreRepository genres, CountryRepository countries) {
        this.genres = genres;
        this.countries = countries;
    }

    @GetMapping("/genres")
    public ApiResponse<List<Genre>> genres() {
        return ApiResponse.success(genres.findAll(org.springframework.data.domain.Sort.by("name")));
    }

    @GetMapping("/countries")
    public ApiResponse<List<Country>> countries() {
        return ApiResponse.success(countries.findAll(org.springframework.data.domain.Sort.by("name")));
    }
}
