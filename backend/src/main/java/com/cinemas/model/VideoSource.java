package com.cinemas.model;
import com.cinemas.enums.VideoQuality;
import com.cinemas.converter.VideoQualityConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import org.hibernate.annotations.ColumnTransformer;
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
    @ColumnTransformer(read = "quality::text", write = "?::video_quality")
    @Column(name="quality", nullable=false, columnDefinition="video_quality")
    private VideoQuality quality=VideoQuality.P720;
    @Column(name="url", nullable=false)
    private String url;
    @Column(name="server_name")
    private String serverName="Server 1";
    @Column(name="is_active", nullable=false)
    private boolean active=true;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}



