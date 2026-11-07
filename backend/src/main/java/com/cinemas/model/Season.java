package com.cinemas.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seasons", uniqueConstraints = @UniqueConstraint(columnNames = { "movie_id", "season_number" }))
@Getter
@Setter
@NoArgsConstructor
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;

    @Column(name = "title")
    private String title;
}

