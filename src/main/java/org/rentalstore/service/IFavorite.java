package org.rentalstore.service;

import org.rentalstore.entity.Favorite;
import org.springframework.http.ResponseEntity;

public interface IFavorite {
    ResponseEntity<?> save(int idUser, Long idProduct, int quantity);
    ResponseEntity<?> update(int idFavorite);
    ResponseEntity<?> delete(int id);
    ResponseEntity<?> findById(int id);
    ResponseEntity<?> countAllByProductId(int productId);
}
