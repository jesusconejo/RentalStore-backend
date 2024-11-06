package org.rentalstore.repository;

import org.rentalstore.entity.Reservation;
import org.rentalstore.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    Set<Reservation> findByUser(Optional<User> userOptional);
}
