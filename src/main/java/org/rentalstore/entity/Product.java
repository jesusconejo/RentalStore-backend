package org.rentalstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private int stock;
    private String imagePath;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @OneToMany(mappedBy = "product")
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Favorite> favorites = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Reservation> reservations = new HashSet<>();

}
