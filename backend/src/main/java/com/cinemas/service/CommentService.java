package com.cinemas.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinemas.dtoresponse.CommentResponse;
import com.cinemas.dtorequest.CommentRequest;
import com.cinemas.exception.ForBiddenException;
import com.cinemas.exception.NotFoundException;
import com.cinemas.model.Comment;
import com.cinemas.repository.CommentRepository;
import com.cinemas.repository.EpisodeRepository;
import com.cinemas.repository.MovieRepository;
import com.cinemas.repository.ProfileRepository;

@Service
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository comments;
    private final MovieRepository movies;
    private final EpisodeRepository episodes;
    private final ProfileRepository profiles;

    public CommentService(CommentRepository comments, MovieRepository movies, EpisodeRepository episodes,
            ProfileRepository profiles) {
        this.comments = comments;
        this.movies = movies;
        this.episodes = episodes;
        this.profiles = profiles;
    }

    public Page<CommentResponse> list(Long movieId, int page, int size) {
        var movie = movies.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found"));
        if (movie.getStatus() != com.cinemas.enums.MovieStatus.published
                && movie.getStatus() != com.cinemas.enums.MovieStatus.upcoming) {
            throw new NotFoundException("Movie not found");
        }
        return comments.findByMovie_IdAndHiddenFalseAndParentIsNullOrderByCreatedAtDesc(movieId,
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), Sort.unsorted()))
                .map(CommentResponse::from);
    }

    @Transactional
    public CommentResponse create(UUID userId, Long movieId, CommentRequest request) {
        var user = profiles.findById(userId).orElseThrow(() -> new NotFoundException("User profile not found"));
        if (user.isBanned()) throw new ForBiddenException("This account cannot post comments");
        var movie = movies.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found"));
        if (movie.getStatus() != com.cinemas.enums.MovieStatus.published
                && movie.getStatus() != com.cinemas.enums.MovieStatus.upcoming) {
            throw new NotFoundException("Movie not found");
        }
        if (request.episodeId() != null) {
            var episode = episodes.findById(request.episodeId()).orElseThrow(() -> new NotFoundException("Episode not found"));
            if (!episode.getMovie().getId().equals(movieId)) throw new com.cinemas.exception.BadRequestException("Episode does not belong to this movie");
        }
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setContent(request.content().trim());
        comment.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        if (request.episodeId() != null) comment.setEpisode(episodes.getReferenceById(request.episodeId()));
        if (request.parentId() != null) {
            Comment parent = comments.findById(request.parentId()).orElseThrow(() -> new NotFoundException("Parent comment not found"));
            if (!parent.getMovie().getId().equals(movieId)) throw new com.cinemas.exception.BadRequestException("Parent comment belongs to another movie");
            comment.setParent(parent);
        }
        return CommentResponse.from(comments.save(comment));
    }

    @Transactional
    public void delete(UUID userId, Long movieId, Long commentId) {
        Comment comment = comments.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (!comment.getMovie().getId().equals(movieId)) throw new NotFoundException("Comment not found");
        if (!comment.getUser().getId().equals(userId)) throw new ForBiddenException("You can only delete your own comments");
        comments.delete(comment);
    }
}
