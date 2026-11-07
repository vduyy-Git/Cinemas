package com.cinemas.model;
import com.cinemas.keys.FavoriteId;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity
@Table(name="favorites")
@IdClass(FavoriteId.class)
@Getter
@Setter
@NoArgsConstructor
public class Favorite {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Profile user;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}



