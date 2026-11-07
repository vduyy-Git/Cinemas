package com.cinemas.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.CommentResponse;
import com.cinemas.dtorequest.CommentRequest;
import com.cinemas.service.CommentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/movies/{movieId}/comments")
public class CommentController {
    private final CommentService service;
    public CommentController(CommentService service) { this.service = service; }

    @GetMapping
    public ApiResponse<Page<CommentResponse>> list(@PathVariable Long movieId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.list(movieId, page, size));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<CommentResponse> create(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId,
            @Valid @RequestBody CommentRequest request) {
        return ApiResponse.success("Comment created", service.create(UUID.fromString(jwt.getSubject()), movieId, request));
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long movieId, @PathVariable Long commentId) {
        service.delete(UUID.fromString(jwt.getSubject()), movieId, commentId);
    }
}
