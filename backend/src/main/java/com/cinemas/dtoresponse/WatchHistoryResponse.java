package com.cinemas.dtoresponse;

import java.time.OffsetDateTime;
import com.cinemas.model.WatchHistory;

public record WatchHistoryResponse(Long episodeId, Long movieId, Integer progressSeconds, Integer durationSeconds,
        boolean completed, OffsetDateTime lastWatchedAt) {
    public static WatchHistoryResponse from(WatchHistory item) {
        return new WatchHistoryResponse(item.getEpisode().getId(), item.getMovie().getId(),
                item.getProgressSeconds(), item.getDurationSeconds(), item.isCompleted(), item.getLastWatchedAt());
    }
}
