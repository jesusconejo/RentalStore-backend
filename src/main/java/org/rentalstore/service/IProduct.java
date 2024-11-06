package org.rentalstore.service;

import org.rentalstore.dto.response.ProdcutResponseDTO;
import org.rentalstore.dto.request.ProductDTO;
import org.rentalstore.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProduct {

    ResponseEntity<?> saveProduct(ProductDTO product);
    ResponseEntity<?> deleteProduct(Long id);
    ResponseEntity<?> updateProduct(ProductDTO product, Long id);
    ResponseEntity<List<ProdcutResponseDTO>> getProducts();
    ResponseEntity<?> getProductById(Long id);
    ResponseEntity<?> getProductByName(String name);
    ResponseEntity<List<ProdcutResponseDTO>> getProductsByCategory(String category);
    ResponseEntity<Iterable<Product>> findAllByListIds(List<Long> ids);
}
