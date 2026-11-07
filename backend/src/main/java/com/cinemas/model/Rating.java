package com.cinemas.model;
import com.cinemas.keys.RatingId;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity
@Table(name="ratings")
@IdClass(RatingId.class)
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Profile user;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Column(name="score", nullable=false)
    private Short score;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}



