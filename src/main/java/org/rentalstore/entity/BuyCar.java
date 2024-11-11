package org.rentalstore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "buy_car")
@Data
public class BuyCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Double tax;
    private Double price;
    private Double subTotal;
    private Double total;
    @OneToMany
    private List<Product> products;
    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
