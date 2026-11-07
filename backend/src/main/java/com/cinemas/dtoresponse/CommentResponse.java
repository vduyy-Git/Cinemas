package com.cinemas.dtoresponse;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.cinemas.model.Comment;

public record CommentResponse(Long id, Long movieId, Long episodeId, Long parentId, UUID userId,
        String displayName, String content, OffsetDateTime createdAt) {
    public static CommentResponse from(Comment value) {
        return new CommentResponse(value.getId(), value.getMovie().getId(),
                value.getEpisode() == null ? null : value.getEpisode().getId(),
                value.getParent() == null ? null : value.getParent().getId(), value.getUser().getId(),
                value.getUser().getDisplayName(), value.getContent(), value.getCreatedAt());
    }
}
