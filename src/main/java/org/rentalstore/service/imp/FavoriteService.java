package org.rentalstore.service.imp;

import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.dto.response.ResponseLikeDTO;
import org.rentalstore.entity.Favorite;
import org.rentalstore.entity.Product;
import org.rentalstore.entity.User;
import org.rentalstore.repository.FavoriteRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.repository.UserRepository;
import org.rentalstore.service.IFavorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class FavoriteService implements IFavorite {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
    private final ResponseLikeDTO responseLikeDTO = new ResponseLikeDTO();

    @Autowired
    public FavoriteService(final FavoriteRepository favoriteRepository, UserRepository userRepo, ProductRepository productRepo) {
        this.favoriteRepository = favoriteRepository;
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    @Override
    public ResponseEntity<?> save(int idUser, Long idProduct, int quantity) {
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
        if(favoriteRepository.findByUserAndProduct(user, product).isPresent()) {
            errorResponseDTO.setMessage("Favorite already exists");
            errorResponseDTO.setErrorCode(4);
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        if(quantity<=0 || quantity > 5) {
            errorResponseDTO.setMessage("Quantity must be greater than 0 and less than 6");
            errorResponseDTO.setErrorCode(4);
            return ResponseEntity.badRequest().body(errorResponseDTO);

        }
        Favorite favorite = new Favorite();
        favorite.setUser(user.get());
        favorite.setProduct(product.get());
        favorite.setCreated(new Date());
        favorite.setQuantity(quantity);

        return ResponseEntity.ok(favoriteRepository.save(favorite));
    }

    @Override
    public ResponseEntity<?> update(int idFavorite) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        Optional<Favorite> favorite = favoriteRepository.findById(idFavorite);
        if(favorite.isEmpty()) {
            errorResponseDTO.setMessage("Favorite not found");
            errorResponseDTO.setErrorCode(4);
            return ResponseEntity.badRequest().body(errorResponseDTO);
        }
        return null;
    }

    @Override
    public ResponseEntity<?> delete(int id) {
        return null;
    }

    @Override
    public ResponseEntity<?> findById(int id) {
        return null;
    }

    @Override
    public ResponseEntity<?> countAllByProductId(int productId) {
        return null;
    }
}
