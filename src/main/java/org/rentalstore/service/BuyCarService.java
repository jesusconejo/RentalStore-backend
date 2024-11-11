package org.rentalstore.service;

import org.rentalstore.dto.request.BuyCarDTO;
import org.rentalstore.entity.BuyCar;
import org.rentalstore.entity.Reservation;
import org.rentalstore.repository.BuyCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BuyCarService implements IBuyCar{

    private final BuyCarRepository buyCarRepository;

    @Autowired
    public BuyCarService(BuyCarRepository buyCarRepository) {
        this.buyCarRepository = buyCarRepository;
    }

    @Override
    public ResponseEntity<?> createBuyCar(BuyCarDTO buyCar, Set<Reservation> reservations) {
        BuyCar buyCar1 = new BuyCar();
        buyCar1.setTax(buyCar.getTax());
        buyCar1.setSubTotal(buyCar.getSubTotal());
        buyCar1.setTotal(buyCar.getTotal());

        return ResponseEntity.ok(buyCarRepository.save(buyCar1));
    }
}
