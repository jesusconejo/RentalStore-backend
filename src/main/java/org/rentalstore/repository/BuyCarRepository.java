package org.rentalstore.repository;

import org.rentalstore.entity.BuyCar;
import org.rentalstore.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BuyCarRepository extends CrudRepository<BuyCar, Integer> {
    Optional<BuyCar> findByUser(User user);
}
