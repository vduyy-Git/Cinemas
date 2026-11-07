package com.cinemas.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
@Entity
@Table(name="notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id", nullable=false)
    private Profile user;
    @Column(nullable=false)
    private String title;
    private String body;
    private String link;
    @Column(name="is_read", nullable=false)
    private boolean read;
    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;
}


