package org.rentalstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table
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
    @JoinColumn(name = "category_id")
    private Category category;
    private Date created;
    private Date modified;

}
