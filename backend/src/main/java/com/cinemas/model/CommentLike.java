package com.cinemas.model;
import com.cinemas.keys.CommentLikeId;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="comment_likes")
@IdClass(CommentLikeId.class)
@Getter
@Setter
@NoArgsConstructor
public class CommentLike {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Profile user;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="comment_id")
    private Comment comment;
}



