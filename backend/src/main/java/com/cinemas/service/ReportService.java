package com.cinemas.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cinemas.dtoresponse.ReportResponse;
import com.cinemas.dtorequest.ReportRequest;
import com.cinemas.enums.ReportStatus;
import com.cinemas.exception.BadRequestException;
import com.cinemas.exception.NotFoundException;
import com.cinemas.model.Report;
import com.cinemas.repository.CommentRepository;
import com.cinemas.repository.EpisodeRepository;
import com.cinemas.repository.MovieRepository;
import com.cinemas.repository.ProfileRepository;
import com.cinemas.repository.ReportRepository;

@Service
@Transactional(readOnly = true)
public class ReportService {
    private final ReportRepository reports;
    private final ProfileRepository profiles;
    private final MovieRepository movies;
    private final EpisodeRepository episodes;
    private final CommentRepository comments;
    public ReportService(ReportRepository reports, ProfileRepository profiles, MovieRepository movies,
            EpisodeRepository episodes, CommentRepository comments) {
        this.reports = reports; this.profiles = profiles; this.movies = movies; this.episodes = episodes; this.comments = comments;
    }
    @Transactional
    public ReportResponse create(UUID userId, ReportRequest request) {
        if (request.movieId() == null && request.episodeId() == null && request.commentId() == null)
            throw new BadRequestException("A report must reference a movie, episode, or comment");
        Report report = new Report();
        report.setUser(profiles.findById(userId).orElseThrow(() -> new NotFoundException("User profile not found")));
        if (request.movieId() != null) report.setMovie(movies.findById(request.movieId()).orElseThrow(() -> new NotFoundException("Movie not found")));
        if (request.episodeId() != null) report.setEpisode(episodes.findById(request.episodeId()).orElseThrow(() -> new NotFoundException("Episode not found")));
        if (request.commentId() != null) report.setComment(comments.findById(request.commentId()).orElseThrow(() -> new NotFoundException("Comment not found")));
        report.setReason(request.reason().trim());
        report.setStatus(ReportStatus.pending);
        report.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return ReportResponse.from(reports.save(report));
    }
    public Page<ReportResponse> list(ReportStatus status, Pageable pageable) {
        Page<Report> page = status == null ? reports.findAll(pageable) : reports.findByStatus(status, pageable);
        return page.map(ReportResponse::from);
    }
    @Transactional
    public ReportResponse updateStatus(Long id, ReportStatus status) {
        Report report = reports.findById(id).orElseThrow(() -> new NotFoundException("Report not found"));
        report.setStatus(status);
        return ReportResponse.from(reports.save(report));
    }
}
