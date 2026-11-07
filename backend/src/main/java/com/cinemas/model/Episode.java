package com.cinemas.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
@Entity
@Table(name="episodes")
@Getter
@Setter
@NoArgsConstructor
public class Episode {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="season_id")
    private Season season;
    @Column(name="episode_number", nullable=false)
    private Integer episodeNumber;
    @Column(name="title")
    private String title;
    @Column(name="description")
    private String description;
    @Column(name="thumbnail_url")
    private String thumbnailUrl;
    @Column(name="duration_minutes")
    private Integer durationMinutes;
    @Column(name="published_at")
    private OffsetDateTime publishedAt;
    @Column(name="view_count", nullable=false)
    private Long viewCount=0L;
}


