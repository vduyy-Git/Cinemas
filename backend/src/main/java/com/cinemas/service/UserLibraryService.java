package com.cinemas.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cinemas.dtoresponse.MovieResponse;
import com.cinemas.dtoresponse.WatchHistoryResponse;
import com.cinemas.dtorequest.WatchProgressRequest;
import com.cinemas.exception.BadRequestException;
import com.cinemas.exception.NotFoundException;
import com.cinemas.model.Favorite;
import com.cinemas.model.Rating;
import com.cinemas.model.WatchHistory;
import com.cinemas.repository.EpisodeRepository;
import com.cinemas.repository.FavoriteRepository;
import com.cinemas.repository.MovieRepository;
import com.cinemas.repository.ProfileRepository;
import com.cinemas.repository.RatingRepository;
import com.cinemas.repository.WatchHistoryRepository;

@Service
@Transactional(readOnly = true)
public class UserLibraryService {
    private final ProfileRepository profiles;
    private final MovieRepository movies;
    private final EpisodeRepository episodes;
    private final FavoriteRepository favorites;
    private final RatingRepository ratings;
    private final WatchHistoryRepository history;

    public UserLibraryService(ProfileRepository profiles, MovieRepository movies, EpisodeRepository episodes,
            FavoriteRepository favorites, RatingRepository ratings, WatchHistoryRepository history) {
        this.profiles = profiles;
        this.movies = movies;
        this.episodes = episodes;
        this.favorites = favorites;
        this.ratings = ratings;
        this.history = history;
    }

    public List<MovieResponse> favorites(UUID userId) {
        return favorites.findByUser_IdOrderByCreatedAtDesc(userId).stream()
                .map(favorite -> MovieResponse.from(favorite.getMovie())).toList();
    }

    @Transactional
    public void addFavorite(UUID userId, Long movieId) {
        if (favorites.existsByUser_IdAndMovie_Id(userId, movieId)) return;
        var user = profiles.findById(userId).orElseThrow(() -> new NotFoundException("User profile not found"));
        var movie = movies.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found"));
        if (movie.getStatus() != com.cinemas.enums.MovieStatus.published
                && movie.getStatus() != com.cinemas.enums.MovieStatus.upcoming) {
            throw new NotFoundException("Movie not found");
        }
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMovie(movie);
        favorite.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        favorites.save(favorite);
    }

    @Transactional
    public void removeFavorite(UUID userId, Long movieId) { favorites.deleteByUser_IdAndMovie_Id(userId, movieId); }

    @Transactional
    public void rate(UUID userId, Long movieId, short score) {
        var user = profiles.findById(userId).orElseThrow(() -> new NotFoundException("User profile not found"));
        var movie = movies.findById(movieId).orElseThrow(() -> new NotFoundException("Movie not found"));
        Rating rating = ratings.findById(new com.cinemas.keys.RatingId(userId, movieId)).orElseGet(Rating::new);
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setScore(score);
        rating.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        ratings.save(rating);
    }

    public List<WatchHistoryResponse> history(UUID userId) {
        return history.findByUser_IdOrderByLastWatchedAtDesc(userId).stream().map(WatchHistoryResponse::from).toList();
    }

    @Transactional
    public WatchHistoryResponse saveProgress(UUID userId, Long episodeId, WatchProgressRequest request) {
        var user = profiles.findById(userId).orElseThrow(() -> new NotFoundException("User profile not found"));
        var episode = episodes.findById(episodeId).orElseThrow(() -> new NotFoundException("Episode not found"));
        var movie = episode.getMovie();
        if (movie.getStatus() != com.cinemas.enums.MovieStatus.published) {
            throw new NotFoundException("Episode not found");
        }
        if (request.durationSeconds() != null && request.progressSeconds() > request.durationSeconds()) {
            throw new BadRequestException("Progress cannot exceed duration");
        }
        WatchHistory item = history.findByUser_IdAndEpisode_Id(userId, episodeId).orElseGet(WatchHistory::new);
        item.setUser(user);
        item.setMovie(movie);
        item.setEpisode(episode);
        item.setProgressSeconds(request.progressSeconds());
        item.setDurationSeconds(request.durationSeconds());
        item.setCompleted(request.durationSeconds() != null && request.progressSeconds() >= request.durationSeconds());
        item.setLastWatchedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return WatchHistoryResponse.from(history.save(item));
    }
}
