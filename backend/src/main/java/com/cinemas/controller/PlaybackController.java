package com.cinemas.controller;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cinemas.dtoresponse.ApiResponse;
import com.cinemas.dtoresponse.EpisodeResponse;
import com.cinemas.dtoresponse.PlaybackResponse;
import com.cinemas.enums.MovieStatus;
import com.cinemas.exception.NotFoundException;
import com.cinemas.repository.EpisodeRepository;
import com.cinemas.repository.MovieRepository;
import com.cinemas.repository.SubtitleRepository;
import com.cinemas.repository.VideoSourceRepository;

@RestController
@RequestMapping("/")
public class PlaybackController {
    private final MovieRepository movies;
    private final EpisodeRepository episodes;
    private final VideoSourceRepository sources;
    private final SubtitleRepository subtitles;

    public PlaybackController(MovieRepository movies, EpisodeRepository episodes, VideoSourceRepository sources,
            SubtitleRepository subtitles) {
        this.movies = movies;
        this.episodes = episodes;
        this.sources = sources;
        this.subtitles = subtitles;
    }

    @GetMapping("/movies/{movieId}/episodes")
    @Transactional(readOnly = true)
    public ApiResponse<List<EpisodeResponse>> episodes(@PathVariable Long movieId) {
        var movie = movies.findById(movieId).filter(value -> value.getStatus() == MovieStatus.published)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        return ApiResponse.success(episodes.findByMovie_IdOrderByEpisodeNumberAsc(movie.getId()).stream()
                .map(EpisodeResponse::from).toList());
    }

    @GetMapping("/episodes/{episodeId}/playback")
    @Transactional(readOnly = true)
    public ApiResponse<PlaybackResponse> playback(@PathVariable Long episodeId) {
        var episode = episodes.findById(episodeId)
                .filter(value -> value.getMovie().getStatus() == MovieStatus.published)
                .orElseThrow(() -> new NotFoundException("Episode not found"));
        var videoItems = sources.findByEpisode_IdAndActiveTrue(episodeId).stream()
                .map(value -> new PlaybackResponse.VideoSourceResponse(value.getQuality().getDatabaseValue(),
                        value.getUrl(), value.getServerName())).toList();
        var subtitleItems = subtitles.findByEpisode_Id(episodeId).stream()
                .map(value -> new PlaybackResponse.SubtitleResponse(value.getLanguageCode(), value.getLabel(),
                        value.getUrl(), value.isDefaultSubtitle())).toList();
        return ApiResponse.success(new PlaybackResponse(episodeId, videoItems, subtitleItems));
    }
}
