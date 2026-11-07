package com.cinemas.repository;

import com.cinemas.model.MovieGenre;
import com.cinemas.keys.MovieGenreId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, MovieGenreId> {
    List<MovieGenre> findByMovie_Id(Long movieId);

    List<MovieGenre> findByGenre_Id(Integer genreId);
}


