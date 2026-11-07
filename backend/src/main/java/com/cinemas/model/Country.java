package com.cinemas.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="countries")
@Getter
@Setter
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false, unique=true)
    private String name;
    @Column(name="slug", nullable=false, unique=true)
    private String slug;
    @Column(name="code", length=2)
    private String code;
}


