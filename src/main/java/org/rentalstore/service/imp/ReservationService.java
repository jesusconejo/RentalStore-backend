package org.rentalstore.service.imp;

import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.dto.request.ReservationDTO;
import org.rentalstore.dto.response.BuyCarResponse;
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
    private final Double TAX = 16.0;
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
        reservation.setEnd(plusDays(fromStringToDate(reservationDTO.getStartReservation()),7));
        List<Product> productList =new ArrayList<>();
        Optional<BuyCar>  buyCar=buyCarRepository.findByUser(user.get());
        BuyCarResponse buyCarResponse = new BuyCarResponse();
        if(buyCar.isEmpty()){
            LOGGER.info("Buy car not found");
            BuyCar buyCar1 = new BuyCar();
            productList.add(product.get());
            buyCar1.setUser(user.get());
            buyCar1.setSubTotal(getSubTotal(productList));
            buyCar1.setTotal(getTotal(productList));
            buyCar1.setPrice(product.get().getPrice());
            buyCar1.setTax(TAX);
            buyCar1.setProducts(productList);
            buyCarRepository.save(buyCar1);
            reservation.setBuyCar(buyCar1);
            buyCarResponse.setProducts(buyCar1.getProducts());
            buyCarResponse.setSubTotal(buyCar1.getSubTotal());
            buyCarResponse.setTotal(buyCar1.getTotal());
            buyCarResponse.setPrice(buyCar1.getPrice());
            buyCarResponse.setTax(buyCar1.getTax());
        }else{
            productList.addAll(buyCar.get().getProducts());
            productList.add(product.get());
            buyCar.get().setProducts(productList);
            buyCar.get().setUser(user.get());
            buyCar.get().setSubTotal(getSubTotal(productList));
            buyCar.get().setTotal(getTotal(productList));
            buyCar.get().setPrice(product.get().getPrice());
            buyCar.get().setTax(TAX);
            buyCarRepository.save(buyCar.get());
            reservation.setBuyCar(buyCar.get());
            buyCarResponse.setProducts(buyCar.get().getProducts());
            buyCarResponse.setSubTotal(buyCar.get().getSubTotal());
            buyCarResponse.setTotal(buyCar.get().getTotal());
            buyCarResponse.setPrice(buyCar.get().getPrice());
            buyCarResponse.setTax(buyCar.get().getTax());
        }


        reservation =reservationRepository.save(reservation);
        ReservationResponseDTO res = new ReservationResponseDTO();
        res.setIdresrvation(reservation.getId());
        res.setUserId(reservation.getUser().getId());
        res.setProductId(product.get().getId());
        res.setStartReservation(reservation.getStart());
        res.setEndReservation(reservation.getEnd());
        res.setBuyCar(buyCarResponse);

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

    private Double getTotal(List<Product> productList) {
        Double total = 0.0;
        for (Product p : productList) {
            total += p.getPrice() ;

        }
        total += total * (TAX / 100);
        return total;
    }

    private Double getSubTotal(List<Product> productList) {
        Double total = 0.0;
        for (Product p : productList) {
            total += p.getPrice() ;
        }
        return total;
    }
}
