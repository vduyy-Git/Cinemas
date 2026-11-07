package com.cinemas.repository;

import com.cinemas.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    List<Banner> findByActiveTrueOrderBySortOrderAsc();
}


