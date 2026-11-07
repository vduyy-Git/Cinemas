package com.cinemas.model;
import com.cinemas.enums.PersonRole;
import com.cinemas.keys.MoviePersonId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
@Entity
@Table(name="movie_people")
@IdClass(MoviePersonId.class)
@Getter
@Setter
@NoArgsConstructor
public class MoviePerson {
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;
    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="person_id")
    private Person person;
    @Id
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable=false, columnDefinition="person_role")
    private PersonRole role;
    @Column(name="character_name")
    private String characterName;
    @Column(name="sort_order")
    private Integer sortOrder=0;
}



