package org.rentalstore.service;

import org.rentalstore.dto.request.ReservationDTO;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.Reservation;
import org.rentalstore.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface IReservation {

    ResponseEntity<?> reserve(ReservationDTO reservationDTO);
    ResponseEntity<?> delete(int reservationId);
    ResponseEntity<?> getReservationByUser(int idUser);
}
