package com.cinemas.model;
import com.cinemas.enums.MovieType;
import com.cinemas.enums.MovieStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name="movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="type", nullable=false, columnDefinition="movie_type")
    private MovieType type=MovieType.movie;
    @Column(name="title", nullable=false)
    private String title;
    @Column(name="original_title")
    private String originalTitle;
    @Column(name="slug", nullable=false, unique=true)
    private String slug;
    @Column(name="description")
    private String description;
    @Column(name="poster_url")
    private String posterUrl;
    @Column(name="backdrop_url")
    private String backdropUrl;
    @Column(name="trailer_url")
    private String trailerUrl;
    @Column(name="release_year")
    private Integer releaseYear;
    @Column(name="duration_minutes")
    private Integer durationMinutes;
    @Column(name="age_rating")
    private String ageRating;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="country_id")
    private Country country;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name="status", nullable=false, columnDefinition="movie_status")
    private MovieStatus status=MovieStatus.draft;
    @Column(name="view_count", nullable=false)
    private Long viewCount=0L;
    @Column(name="rating_avg", nullable=false, precision=4, scale=2)
    private BigDecimal ratingAvg=BigDecimal.ZERO;
    @Column(name="rating_count", nullable=false)
    private Integer ratingCount=0;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
    @Column(name="updated_at", nullable=false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}



