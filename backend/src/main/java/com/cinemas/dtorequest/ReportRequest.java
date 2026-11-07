package com.cinemas.dtorequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReportRequest(Long movieId, Long episodeId, Long commentId,
        @NotBlank @Size(max = 2000) String reason) { }
