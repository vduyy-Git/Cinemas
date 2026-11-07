package com.cinemas.repository;

import com.cinemas.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByMovie_IdAndHiddenFalseAndParentIsNullOrderByCreatedAtDesc(
            Long movieId,
            Pageable pageable
    );

    Page<Comment> findByParent_IdAndHiddenFalseOrderByCreatedAtAsc(
            Long parentId,
            Pageable pageable
    );
}


