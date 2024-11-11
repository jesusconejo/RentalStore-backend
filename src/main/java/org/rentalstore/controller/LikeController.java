package org.rentalstore.controller;

import org.rentalstore.service.imp.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-like")
@CrossOrigin(origins = "http://localhost:5173/")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(final LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestParam int idUser, @RequestParam Long idProduct) {
        return likeService.saveLike(idUser, idProduct);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam int idUser, @RequestParam Long idProduct) {
        return likeService.deleteLike(idUser, idProduct);
    }

    @GetMapping("/getCountAllByProduct")
    public ResponseEntity<?> getCountAllByProduct(@RequestParam Long idProduct) {
        return likeService.getCountAllByProduct(idProduct);
    }
}
