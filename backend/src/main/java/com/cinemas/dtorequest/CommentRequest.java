package com.cinemas.dtorequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(Long episodeId, Long parentId, @NotBlank @Size(max = 5000) String content) { }
