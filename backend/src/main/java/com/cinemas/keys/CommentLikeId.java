package com.cinemas.keys;
import java.io.Serializable;
import java.util.UUID;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    private UUID user;
    private Long comment;
}




