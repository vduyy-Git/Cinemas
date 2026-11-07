package com.cinemas.repository;

import com.cinemas.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    Optional<Collection> findBySlug(String slug);

    List<Collection> findByActiveTrueOrderBySortOrderAsc();
}


