package com.cinemas.keys;
import java.io.Serializable;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovieGenreId implements Serializable {
    private Long movie;
    private Integer genre;
}




