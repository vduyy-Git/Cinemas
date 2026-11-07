package com.cinemas.dtorequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WatchProgressRequest(@NotNull @Min(0) Integer progressSeconds, @Min(1) Integer durationSeconds) { }
