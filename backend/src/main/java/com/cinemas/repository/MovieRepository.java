package com.cinemas.repository;

import com.cinemas.model.Movie;
import com.cinemas.enums.MovieStatus;
import com.cinemas.enums.MovieType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findBySlug(String slug);

    Page<Movie> findByStatusIn(Collection<MovieStatus> statuses, Pageable pageable);

    Page<Movie> findByStatusInAndType(
            Collection<MovieStatus> statuses,
            MovieType type,
            Pageable pageable
    );

    boolean existsBySlug(String slug);
}


