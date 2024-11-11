package org.rentalstore.controller;

import org.rentalstore.service.imp.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-favorite")
@CrossOrigin(origins = "http://localhost:5173/")
public class FavoriteController {

    private final FavoriteService favoriteService;
    @Autowired
    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/qualification")
    public ResponseEntity<?> addFavorite(@RequestParam int idUser, @RequestParam Long idProduct, @RequestParam int quantity) {
        return favoriteService.save(idUser, idProduct, quantity);
    }
}
