package com.cinemas.model;
import com.cinemas.enums.VideoQuality;
import com.cinemas.converter.VideoQualityConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
@Entity
@Table(name="video_sources")
@Getter
@Setter
@NoArgsConstructor
public class VideoSource {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="episode_id", nullable=false)
    private Episode episode;
    @Convert(converter=VideoQualityConverter.class)
    @Column(nullable=false, columnDefinition="video_quality")
    private VideoQuality quality=VideoQuality.P720;
    @Column(nullable=false)
    private String url;
    @Column(name="server_name")
    private String serverName="Server 1";
    @Column(name="is_active", nullable=false)
    private boolean active=true;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}



