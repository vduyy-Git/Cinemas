package com.cinemas.dtoresponse;

import java.util.List;

public record PlaybackResponse(Long episodeId, List<VideoSourceResponse> sources, List<SubtitleResponse> subtitles) {
    public record VideoSourceResponse(String quality, String url, String serverName) { }
    public record SubtitleResponse(String languageCode, String label, String url, boolean isDefault) { }
}
