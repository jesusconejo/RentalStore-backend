package org.rentalstore.repository;

import org.rentalstore.entity.Favorite;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {

    Optional<Favorite> findByUserAndProduct(Optional<User> user, Optional<Product> product);

    Integer countAllByProduct(Optional<Product> product);

    @Query("SELECT SUM(f.quantity) FROM Favorite f WHERE f.product = ?1")
    Integer sumByProduct(Product product);

    Set<Favorite> findAllByUser(Optional<User> userOptional);

    Optional<Favorite> findByProduct(Optional<Product> productOptional);
}
