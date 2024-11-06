package org.rentalstore.service.imp;

import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.dto.request.ReservationDTO;
import org.rentalstore.dto.response.ReservationResponseDTO;
import org.rentalstore.entity.BuyCar;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.Reservation;
import org.rentalstore.entity.User;
import org.rentalstore.repository.BuyCarRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.repository.ReservationRepository;
import org.rentalstore.repository.UserRepository;
import org.rentalstore.service.BuyCarService;
import org.rentalstore.service.IReservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservationService implements IReservation {
    private final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BuyCarRepository buyCarRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, ProductRepository productRepository, BuyCarService buyCarService, BuyCarRepository buyCarRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.buyCarRepository = buyCarRepository;
    }

    @Override
    public ResponseEntity<?> reserve(ReservationDTO reservationDTO) {
        Optional<User> user = userRepository.findById(reservationDTO.getIdUser());
        Optional<Product> product = productRepository.findById(reservationDTO.getIdProduct());
        ErrorResponseDTO err = new ErrorResponseDTO();
        if (user.isEmpty() || product.isEmpty()) {
            err.setMessage("User not found");
            err.setErrorCode(4);
            return ResponseEntity.status(400).body(err);
        }
       // LOGGER.info("Algo debeb llegar "+user.get().getBuyCar());
        Reservation reservation = new Reservation();
        reservation.setProduct(product.get());
        reservation.setUser(user.get());
        reservation.setCreated(new Date());
        reservation.setStart(fromStringToDate(reservationDTO.getStartReservation()));
        reservation.setEnd(fromStringToDate(reservationDTO.getEndReservation()));
        List<Product> productList =new ArrayList<>();
        Optional<BuyCar>  buyCar=buyCarRepository.findByUser(user.get());
        if(buyCar.isEmpty()){
            productList.add(product.get());
            buyCar.get().setUser(user.get());
            buyCar.get().setTax(16.0);
            buyCar.get().setProducts(productList);
            buyCarRepository.save(buyCar.get());
        }else{
            for(Product p: buyCar.get().getProducts()){
                productList.add(p);
            }
            buyCar.get().setProducts(productList);
            buyCarRepository.save(buyCar.get());
        }


        reservation =reservationRepository.save(reservation);
        ReservationResponseDTO res = new ReservationResponseDTO();
        res.setIdresrvation(reservation.getId());
        res.setUserId(reservation.getUser().getId());
        res.setProductId(product.get().getId());
        res.setStartReservation(reservation.getStart());
        res.setEndReservation(reservation.getEnd());
        return ResponseEntity.status(200).body(res);
    }

    @Override
    public ResponseEntity<?> delete(int idReservation) {
        return null;
    }

    @Override
    public ResponseEntity<?> getReservationByUser(int idUser) {
        Optional<User> user = userRepository.findById(idUser);
        ErrorResponseDTO err = new ErrorResponseDTO();
        if (user.isEmpty()) {
            err.setMessage("User not found");
            err.setErrorCode(4);
            return ResponseEntity.status(404).body(err);
        }
        Set<Reservation> reservation = reservationRepository.findByUser(user);
        return ResponseEntity.status(200).body(reservation);
    }
    private  Date fromStringToDate(String dateString) {
        LOGGER.info("dateString: {}", dateString);
        if (dateString == null || dateString.trim().isEmpty()) {
            LOGGER.info("La fecha en formato String no puede ser nula o vacía");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        // Convertir LocalDate a Date
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private  Date plusDays(Date date, int days) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate newDate = localDate.plusDays(days);
        return Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
