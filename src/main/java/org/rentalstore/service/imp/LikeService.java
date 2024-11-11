package org.rentalstore.service.imp;

import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.dto.response.ResponseLikeDTO;
import org.rentalstore.dto.response.UserResponseDTO;
import org.rentalstore.entity.Like;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.User;
import org.rentalstore.repository.LikeRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.repository.UserRepository;
import org.rentalstore.service.ILike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class LikeService implements ILike {

    private final LikeRepository likeRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
    private final ResponseLikeDTO responseLikeDTO = new ResponseLikeDTO();
    @Autowired
    public LikeService(LikeRepository likeRepo, UserRepository userRepo, ProductRepository productRepo) {
        this.likeRepo = likeRepo;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public ResponseEntity<?> saveLike(int idUser, Long idProduct) {
        if (idUser == 0 || idProduct == 0) {
            ResponseEntity.badRequest().build();
        }
        Optional<User> user = userRepo.findById(idUser);
        if (user.isEmpty()) {
            errorResponseDTO.setMessage("User not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id user not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        Optional<Product> product = productRepo.findById(idProduct);
        if (product.isEmpty()) {
            errorResponseDTO.setMessage("Product not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id product not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        if (likeRepo.findByUserAndProduct(user, product).isPresent()) {
            return ResponseEntity.badRequest().body("Like already exists");
        }
        Like like = new Like();
        like.setUser(user.get());
        like.setProduct(product.get());
        like.setLikeDate(new Date());
        like = likeRepo.save(like);

        responseLikeDTO.setId(like.getId());
        responseLikeDTO.setProductId(like.getProduct().getId());
        responseLikeDTO.setUserId(like.getUser().getId());
        responseLikeDTO.setDate(like.getLikeDate());
        return ResponseEntity.ok(responseLikeDTO);
    }

    @Override
    public ResponseEntity<?> deleteLike(int idUser, Long idProduct) {

        if (idUser == 0 || idProduct == 0) {
            ResponseEntity.badRequest().build();
        }
        Optional<User> user = userRepo.findById(idUser);
        if (user.isEmpty()) {
            errorResponseDTO.setMessage("User not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id user not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        Optional<Product> product = productRepo.findById(idProduct);
        if (product.isEmpty()) {
            errorResponseDTO.setMessage("Product not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id product not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        Optional<Like> like = likeRepo.findByUserAndProduct(user, product);

        if (like.isEmpty()) {
            errorResponseDTO.setMessage("Like not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id like not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        likeRepo.delete(like.get());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> getCountAllByProduct(long idProduct) {
        Optional<Product> product = productRepo.findById(idProduct);
        if (product.isEmpty()) {
            errorResponseDTO.setMessage("Product not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id product not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        Integer likeCountAll = likeRepo.countAllByProduct(product.get());
        return ResponseEntity.ok(likeCountAll);
    }

    @Override
    public ResponseEntity<?> getAllByUser(int idUser) {

        Optional<User> user = userRepo.findById(idUser);
        if (user.isEmpty()) {
            errorResponseDTO.setMessage("User not found");
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id user not found");
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        Integer countLikeUser = likeRepo.countAllByUser(user.get());
        return ResponseEntity.ok(countLikeUser);
    }
}
