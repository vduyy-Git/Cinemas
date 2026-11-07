package com.cinemas.repository;

import com.cinemas.model.MoviePerson;
import com.cinemas.keys.MoviePersonId;
import com.cinemas.enums.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoviePersonRepository extends JpaRepository<MoviePerson, MoviePersonId> {
    List<MoviePerson> findByMovie_IdOrderBySortOrderAsc(Long movieId);

    List<MoviePerson> findByPerson_IdAndRole(Long personId, PersonRole role);
}


