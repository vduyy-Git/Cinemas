package com.cinemas.dtoresponse;

import java.time.OffsetDateTime;
import com.cinemas.enums.ReportStatus;
import com.cinemas.model.Report;

public record ReportResponse(Long id, Long movieId, Long episodeId, Long commentId, String reason,
        ReportStatus status, OffsetDateTime createdAt) {
    public static ReportResponse from(Report value) {
        return new ReportResponse(value.getId(), value.getMovie() == null ? null : value.getMovie().getId(),
                value.getEpisode() == null ? null : value.getEpisode().getId(),
                value.getComment() == null ? null : value.getComment().getId(), value.getReason(),
                value.getStatus(), value.getCreatedAt());
    }
}
