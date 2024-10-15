package org.rentalstore.repository;

import org.rentalstore.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
   Optional<User> findByUsername(String username);

   Optional<User> findByEmail(String email);
}
