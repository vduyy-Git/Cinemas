package com.cinemas.service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cinemas.dtoresponse.NotificationResponse;
import com.cinemas.exception.NotFoundException;
import com.cinemas.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notifications;
    public NotificationService(NotificationRepository notifications) { this.notifications = notifications; }
    public Page<NotificationResponse> list(UUID userId, Pageable pageable) {
        return notifications.findByUser_IdOrderByCreatedAtDesc(userId, pageable).map(NotificationResponse::from);
    }
    public long unreadCount(UUID userId) { return notifications.countByUser_IdAndReadFalse(userId); }
    @Transactional
    public NotificationResponse markRead(UUID userId, Long id) {
        var notification = notifications.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setRead(true);
        return NotificationResponse.from(notifications.save(notification));
    }
}
