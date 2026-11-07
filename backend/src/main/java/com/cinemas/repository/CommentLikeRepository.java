package com.cinemas.repository;

import com.cinemas.model.CommentLike;
import com.cinemas.keys.CommentLikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentLikeRepository extends JpaRepository<CommentLike, CommentLikeId> {
    boolean existsByUser_IdAndComment_Id(UUID userId, Long commentId);

    long countByComment_Id(Long commentId);
}


