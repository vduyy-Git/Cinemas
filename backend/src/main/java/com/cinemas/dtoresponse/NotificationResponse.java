package com.cinemas.dtoresponse;

import java.time.OffsetDateTime;
import com.cinemas.model.Notification;

public record NotificationResponse(Long id, String title, String body, String link,
        boolean read, OffsetDateTime createdAt) {
    public static NotificationResponse from(Notification value) {
        return new NotificationResponse(value.getId(), value.getTitle(), value.getBody(), value.getLink(),
                value.isRead(), value.getCreatedAt());
    }
}
