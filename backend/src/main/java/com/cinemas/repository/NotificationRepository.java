package com.cinemas.repository;

import com.cinemas.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUser_IdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    long countByUser_IdAndReadFalse(UUID userId);

    Optional<Notification> findByIdAndUser_Id(Long id, UUID userId);
}


