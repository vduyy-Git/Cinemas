package com.cinemas.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.ReportResponse;
import com.cinemas.dtorequest.ReportRequest;
import com.cinemas.dtorequest.ReportStatusRequest;
import com.cinemas.enums.ReportStatus;
import com.cinemas.service.ReportService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
@Validated
public class ReportController {
    private final ReportService service;
    public ReportController(ReportService service) { this.service = service; }
    @PostMapping("/reports")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReportResponse> create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.success("Report submitted", service.create(UUID.fromString(jwt.getSubject()), request));
    }
    @GetMapping("/admin/reports")
    @PreAuthorize("hasAnyRole('admin','moderator')")
    public ApiResponse<Page<ReportResponse>> list(@RequestParam(required = false) ReportStatus status,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(service.list(status, PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100),
                Sort.by(Sort.Direction.DESC, "createdAt"))));
    }
    @PatchMapping("/admin/reports/{id}")
    @PreAuthorize("hasAnyRole('admin','moderator')")
    public ApiResponse<ReportResponse> update(@PathVariable Long id, @Valid @RequestBody ReportStatusRequest request) {
        return ApiResponse.success(service.updateStatus(id, request.status()));
    }
}
