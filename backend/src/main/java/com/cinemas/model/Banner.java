package com.cinemas.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="banners")
@Getter
@Setter
@NoArgsConstructor
public class Banner {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Column(name="image_url", nullable=false)
    private String imageUrl;
    private String title;
    private String link;
    @Column(name="sort_order")
    private Integer sortOrder=0;
    @Column(name="is_active", nullable=false)
    private boolean active=true;
}


