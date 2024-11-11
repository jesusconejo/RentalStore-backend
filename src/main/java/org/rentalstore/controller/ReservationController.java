package org.rentalstore.controller;

import org.rentalstore.dto.request.ReservationDTO;
import org.rentalstore.service.imp.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-reservation")
public class ReservationController {
    private final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);
    private final ReservationService reservationService;
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReservationDTO reservation) {
        return reservationService.reserve(reservation);
    }

    @GetMapping("/getReservationsByUser")
    public ResponseEntity<?> getReservationsByUser(@RequestParam int userId) {
        return reservationService.getReservationByUser(userId);
    }
}
