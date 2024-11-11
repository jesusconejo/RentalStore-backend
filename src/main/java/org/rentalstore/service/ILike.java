package org.rentalstore.service;

import org.apache.coyote.Response;
import org.rentalstore.entity.Like;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.User;
import org.springframework.http.ResponseEntity;

public interface ILike {

    ResponseEntity<?> saveLike(int idUser, Long idProduct);
    ResponseEntity<?> deleteLike(int idUser, Long idProduct);
    ResponseEntity<?> getCountAllByProduct(long idProduct);
    ResponseEntity<?> getAllByUser(int user);
}
