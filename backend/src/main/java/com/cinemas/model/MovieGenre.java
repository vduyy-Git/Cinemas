package com.cinemas.model;
import com.cinemas.keys.MovieGenreId;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="movie_genres")
@IdClass(MovieGenreId.class)
@Getter
@Setter
@NoArgsConstructor
public class MovieGenre {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="genre_id")
    private Genre genre;
}



