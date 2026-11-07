package com.cinemas.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
@Entity
@Table(name="people")
@Getter
@Setter
@NoArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="slug", nullable=false, unique=true)
    private String slug;
    @Column(name="avatar_url")
    private String avatarUrl;
    @Column(name="bio")
    private String bio;
    @Column(name="birth_date")
    private LocalDate birthDate;
}


