package com.cinemas.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.NotificationResponse;
import com.cinemas.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service = service; }
    @GetMapping
    public ApiResponse<Page<NotificationResponse>> list(@AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.by(Sort.Direction.DESC, "createdAt"));
        return ApiResponse.success(service.list(userId(jwt), pageable));
    }
    @GetMapping("/unread-count")
    public ApiResponse<Long> unread(@AuthenticationPrincipal Jwt jwt) {
        return ApiResponse.success(service.unreadCount(userId(jwt)));
    }
    @PatchMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markRead(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return ApiResponse.success(service.markRead(userId(jwt), id));
    }
    private UUID userId(Jwt jwt) { return UUID.fromString(jwt.getSubject()); }
}
