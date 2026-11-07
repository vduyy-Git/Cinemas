package com.cinemas.service;

import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinemas.dtoresponse.MovieResponse;
import com.cinemas.dtorequest.MovieRequest;
import com.cinemas.enums.MovieStatus;
import com.cinemas.enums.MovieType;
import com.cinemas.exception.NotFoundException;
import com.cinemas.model.Country;
import com.cinemas.model.Movie;
import com.cinemas.repository.CountryRepository;
import com.cinemas.repository.MovieRepository;


@Service
@Transactional(readOnly = true)
public class MovieService {
    private final MovieRepository movies;
    private final CountryRepository countries;

    public MovieService(MovieRepository movies, CountryRepository countries) {
        this.movies = movies;
        this.countries = countries;
    }

    public Page<MovieResponse> browse(String query, MovieType type, Integer year, Integer countryId, int page, int size) {
        int safePage = Math.max(0, page);
        int safeSize = Math.min(Math.max(1, size), 100);
        Specification<Movie> spec = (root, criteria, cb) -> {
            criteria.distinct(true);
            var status = root.get("status").in(MovieStatus.published, MovieStatus.upcoming);
            var terms = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            terms.add(status);
            if (type != null) terms.add(cb.equal(root.get("type"), type));
            if (year != null) terms.add(cb.equal(root.get("releaseYear"), year));
            if (countryId != null) terms.add(cb.equal(root.get("country").get("id"), countryId));
            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.trim().toLowerCase(Locale.ROOT) + "%";
                terms.add(cb.or(cb.like(cb.lower(root.get("title")), pattern),
                        cb.like(cb.lower(root.get("originalTitle")), pattern)));
            }
            return cb.and(terms.toArray(jakarta.persistence.criteria.Predicate[]::new));
        };
        return movies.findAll(spec, PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(MovieResponse::from);
    }

    public MovieResponse getPublished(String slug) {
        Movie movie = movies.findBySlug(slug).orElseThrow(() -> new NotFoundException("Movie not found"));
        if (movie.getStatus() != MovieStatus.published && movie.getStatus() != MovieStatus.upcoming) {
            throw new NotFoundException("Movie not found");
        }
        return MovieResponse.from(movie);
    }

    @Transactional
    public MovieResponse create(MovieRequest request) {
        if (movies.existsBySlug(request.slug())) throw new com.cinemas.exception.ConflictException("Movie slug already exists");
        Movie movie = new Movie();
        apply(movie, request);
        return MovieResponse.from(movies.save(movie));
    }

    @Transactional
    public MovieResponse update(Long id, MovieRequest request) {
        Movie movie = movies.findById(id).orElseThrow(() -> new NotFoundException("Movie not found"));
        movies.findBySlug(request.slug()).filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> { throw new com.cinemas.exception.ConflictException("Movie slug already exists"); });
        apply(movie, request);
        return MovieResponse.from(movies.save(movie));
    }

    @Transactional
    public void delete(Long id) {
        if (!movies.existsById(id)) throw new NotFoundException("Movie not found");
        movies.deleteById(id);
    }

    private void apply(Movie movie, MovieRequest request) {
        movie.setType(request.type());
        movie.setTitle(request.title().trim());
        movie.setOriginalTitle(request.originalTitle());
        movie.setSlug(request.slug().trim());
        movie.setDescription(request.description());
        movie.setPosterUrl(request.posterUrl());
        movie.setBackdropUrl(request.backdropUrl());
        movie.setTrailerUrl(request.trailerUrl());
        movie.setReleaseYear(request.releaseYear());
        movie.setDurationMinutes(request.durationMinutes());
        movie.setAgeRating(request.ageRating());
        movie.setStatus(request.status());
        if (request.countryId() == null) movie.setCountry(null);
        else {
            Country country = countries.findById(request.countryId())
                    .orElseThrow(() -> new NotFoundException("Country not found"));
            movie.setCountry(country);
        }
    }
}
