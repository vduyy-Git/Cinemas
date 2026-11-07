package com.cinemas.model;
import com.cinemas.keys.CollectionMovieId;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="collection_movies")
@IdClass(CollectionMovieId.class)
@Getter
@Setter
@NoArgsConstructor
public class CollectionMovie {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="collection_id")
    private Collection collection;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Column(name="sort_order")
    private Integer sortOrder=0;
}



