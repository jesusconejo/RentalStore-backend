package org.rentalstore.service;

import org.rentalstore.dto.request.BuyCarDTO;
import org.rentalstore.entity.BuyCar;
import org.rentalstore.entity.Reservation;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface IBuyCar {

    ResponseEntity<?> createBuyCar(BuyCarDTO buyCar, Set<Reservation> reservations);
}
