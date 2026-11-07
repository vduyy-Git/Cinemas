package com.cinemas.repository;

import com.cinemas.model.CollectionMovie;
import com.cinemas.keys.CollectionMovieId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionMovieRepository extends JpaRepository<CollectionMovie, CollectionMovieId> {
    List<CollectionMovie> findByCollection_IdOrderBySortOrderAsc(Integer collectionId);
}


