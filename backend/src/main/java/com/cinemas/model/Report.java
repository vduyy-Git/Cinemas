package com.cinemas.model;
import com.cinemas.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name="reports")
@Getter
@Setter
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Profile user;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="episode_id")
    private Episode episode;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="comment_id")
    private Comment comment;
    @Column(nullable=false)
    private String reason;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable=false, columnDefinition="report_status")
    private ReportStatus status=ReportStatus.pending;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}



