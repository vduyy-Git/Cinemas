package com.cinemas.keys;
import java.io.Serializable;
import java.util.UUID;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RatingId implements Serializable {
    private UUID user;
    private Long movie;
}




