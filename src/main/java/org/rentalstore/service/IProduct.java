package org.rentalstore.service;

import org.rentalstore.dto.ProdcutAdminDTO;
import org.rentalstore.dto.ProductDTO;
import org.rentalstore.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IProduct {

    ResponseEntity<?> saveProduct(ProductDTO product);
    ResponseEntity<?> deleteProduct(Long id);
    ResponseEntity<?> updateProduct(ProductDTO product, Long id);
    ResponseEntity<List<ProdcutAdminDTO>> getProducts();
    ResponseEntity<?> getProductById(Long id);
    ResponseEntity<?> getProductByName(String name);
    ResponseEntity<List<Product>> getProductsByCategory(String category);
}
