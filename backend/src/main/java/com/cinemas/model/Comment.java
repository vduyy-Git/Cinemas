package com.cinemas.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private Profile user;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="movie_id", nullable=false)
    private Movie movie;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="episode_id")
    private Episode episode;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private Comment parent;
    @Column(nullable=false)
    private String content;
    @Column(name="is_hidden", nullable=false)
    private boolean hidden;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}


