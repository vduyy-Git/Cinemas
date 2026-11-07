package com.cinemas.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name="collections")
@Getter
@Setter
@NoArgsConstructor
public class Collection {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="slug", nullable=false, unique=true)
    private String slug;
    @Column(name="description")
    private String description;
    @Column(name="sort_order")
    private Integer sortOrder=0;
    @Column(name="is_active", nullable=false)
    private boolean active=true;
}


