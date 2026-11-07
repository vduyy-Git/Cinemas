package com.cinemas.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="subtitles")
@Getter
@Setter
@NoArgsConstructor
public class Subtitle {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="episode_id", nullable=false)
    private Episode episode;
    @Column(name="language_code", nullable=false)
    private String languageCode;
    @Column(name="label", nullable=false)
    private String label;
    @Column(name="url", nullable=false)
    private String url;
    @Column(name="is_default", nullable=false)
    private boolean defaultSubtitle;
}


