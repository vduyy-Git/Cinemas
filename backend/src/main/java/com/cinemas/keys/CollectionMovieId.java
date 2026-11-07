package com.cinemas.keys;
import java.io.Serializable;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CollectionMovieId implements Serializable {
    private Integer collection;
    private Long movie;
}




