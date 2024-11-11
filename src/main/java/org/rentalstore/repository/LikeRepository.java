package org.rentalstore.repository;

import org.rentalstore.entity.Like;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends CrudRepository<Like, Long> {

    Integer countAllByProduct(Product product);
    Integer countAllByUser(User user);
    Optional<Like> findByUserAndProduct(Optional<User> user, Optional<Product> product);
    Set<Like> findAllByUser(Optional<User> userOptional);
    List<Like> findAllByProduct(Product product);
    List<Like> findAllByUser(User user);

    Optional<Like> findByProduct(Optional<Product> productOptional);
}
