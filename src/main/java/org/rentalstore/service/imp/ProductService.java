package org.rentalstore.service.imp;

import org.rentalstore.dto.response.ProdcutResponseDTO;
import org.rentalstore.dto.request.ProductDTO;
import org.rentalstore.entity.Category;
import org.rentalstore.entity.Favorite;
import org.rentalstore.entity.Like;
import org.rentalstore.entity.Product;
import org.rentalstore.repository.CategoryRepository;
import org.rentalstore.repository.FavoriteRepository;
import org.rentalstore.repository.LikeRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.service.IProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductService implements IProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public ProductService(ProductRepository repo, CategoryRepository catRepo, FavoriteRepository favoriteRepository, LikeRepository likeRepository) {
        this.categoryRepository = catRepo;
        this.productRepository = repo;
        this.favoriteRepository = favoriteRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public ResponseEntity<?> saveProduct(ProductDTO product) {
        // Check if the product already exists
        if (productRepository.findByName(product.getName()).isPresent()) {
            return new ResponseEntity<>("Product already exists", HttpStatus.BAD_REQUEST);
        }

        // Ensure the category is provided and valid
        if (product.getCategory() == null || product.getCategory().intValue() < 0) {
            return new ResponseEntity<>("Category is required", HttpStatus.BAD_REQUEST);
        }

        Optional<Category> categoryOptional = categoryRepository.findById(Integer.valueOf(product.getCategory()));
        if (!categoryOptional.isPresent()) {
            return new ResponseEntity<>("Invalid category", HttpStatus.BAD_REQUEST);
        }

        // Create a new product entity
        Product product1 = new Product();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setPrice(Double.parseDouble(product.getPrice() + ""));  // Ensure price is properly converted
        product1.setImagePath(product.getImagePath());
        product1.setCreated(new Date());
        product1.setStock(Integer.parseInt(product.getStock() + ""));  // Ensure stock is properly converted
        product1.setCategory(categoryOptional.get());  // Set the found category

        // Save the product
        return ResponseEntity.ok(productRepository.save(product1));
    }


    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Optional<Favorite> favorite=  favoriteRepository.findByProduct(productOptional);
            if (favorite.isPresent()) {
                favoriteRepository.delete(favorite.get());
            }
            Optional<Like> like = likeRepository.findByProduct(productOptional);
            if (like.isPresent()) {
                likeRepository.delete(like.get());
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> updateProduct(ProductDTO product, Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {

            Optional<Product> productWithName = productRepository.findByName(product.getName());
            if (productWithName.isPresent() && !productWithName.get().getId().equals(id)) {
                return new ResponseEntity<>("Name Product already exists", HttpStatus.BAD_REQUEST);
            }
            Product product1 = productOptional.get();
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setPrice(product.getPrice());
            product1.setStock(product.getStock());
            product1.setImagePath(product.getImagePath());
            product1.setCategory(categoryRepository.findById(Integer.valueOf(product.getCategory())).get());
            product1.setModified(new Date());
            return ResponseEntity.ok(productRepository.save(product1));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ProdcutResponseDTO>> getProducts() {
        Iterable<Product> productIterable = productRepository.findAll();
        List<ProdcutResponseDTO> productList = new ArrayList<>();

        for (Product product : productIterable) {
            // Crear un objeto DTO y mapear los valores desde Product
            ProdcutResponseDTO productDTO = new ProdcutResponseDTO();
            productDTO.setId(product.getId().intValue()); // Si quieres el id como int
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImagePath(product.getImagePath());

            // Convertir categoría de Integer a String usando tu método o HashMap

            productDTO.setCategory(product.getCategory().getName());
            productDTO.setFavorite(totalQualification(Optional.of(product)));
            // Convertir las fechas de Date a String
            productDTO.setCreated(formatDate(product.getCreated()));
            productDTO.setModified(formatDate(product.getModified()));

            productList.add(productDTO);  // Añadir el DTO a la lista
        }

        return ResponseEntity.ok(productList);
    }


    @Override
    public ResponseEntity<?> getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return ResponseEntity.ok(product);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> getProductByName(String name) {
        Product product = productRepository.findByName(name).orElse(null);
        if (product != null) {
            return ResponseEntity.ok(product);
        }

        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<ProdcutResponseDTO>> getProductsByCategory(String categoryValue) {
        List<ProdcutResponseDTO> productListResponse = new ArrayList<>();
        List<Product> productList = productRepository.findByCategory(categoryRepository.findById(Integer.valueOf(categoryValue)).get());
        for (Product product : productList) {
            ProdcutResponseDTO productDTO = new ProdcutResponseDTO();
            productDTO.setId(product.getId().intValue());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImagePath(product.getImagePath());
            productDTO.setCategory(product.getCategory().getName());
            productDTO.setFavorite(totalQualification(Optional.of(product)));
            productDTO.setCreated(formatDate(product.getCreated()));
            productDTO.setModified(formatDate(product.getModified()));
            productListResponse.add(productDTO);
        }
        return ResponseEntity.ok(productListResponse);
    }

    @Override
    public ResponseEntity<Iterable<Product>> findAllByListIds(List<Long> ids) {
        Iterable<Product> productIterable = productRepository.findAllById(ids);
        return ResponseEntity.ok(productIterable);
    }


    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    private Integer totalQualification(Optional<Product> productOptional) {
        LOGGER.info("Total qualification found: {}", productOptional.isPresent());
        Integer count = favoriteRepository.countAllByProduct(productOptional);
        if (count < 1) {
            return 0;
        }
        Integer sum = favoriteRepository.sumByProduct(productOptional.get());
        LOGGER.info("Total qualification found sum {}and count{}", sum, count);
        return sum / count;
    }


}
