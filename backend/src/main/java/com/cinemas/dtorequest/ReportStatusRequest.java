package com.cinemas.dtorequest;

import com.cinemas.enums.ReportStatus;
import jakarta.validation.constraints.NotNull;

public record ReportStatusRequest(@NotNull ReportStatus status) { }
