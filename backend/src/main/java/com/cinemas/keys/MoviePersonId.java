package com.cinemas.keys;
import com.cinemas.enums.PersonRole;
import java.io.Serializable;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MoviePersonId implements Serializable {
    private Long movie;
    private Long person;
    private PersonRole role;
}




