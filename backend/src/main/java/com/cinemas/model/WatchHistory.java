package com.cinemas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

@Entity
@Table(
    name = "watch_history",
    uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "episode_id" })
)
@Getter
@Setter
@NoArgsConstructor
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Profile user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    @Column(name = "progress_seconds", nullable = false)
    private Integer progressSeconds = 0;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "is_completed", nullable = false)
    private boolean completed;

    @Column(name = "last_watched_at", nullable = false)
    private OffsetDateTime lastWatchedAt;
}

