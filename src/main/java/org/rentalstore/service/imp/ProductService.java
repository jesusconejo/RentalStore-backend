package org.rentalstore.service.imp;

import org.rentalstore.dto.ProductDTO;
import org.rentalstore.entity.Product;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.service.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService implements IProduct {

    private ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository repo) {
        this.productRepository = repo;
    }

    @Override
    public ResponseEntity<?> saveProduct(ProductDTO product) {
        Product product1 = new Product();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setImagePath(product.getImagePath());
        product1.setCreated(new Date());
        product1.setStock(product.getStock());
        product1.setCategory(product.getCategory());
        return  ResponseEntity.ok( productRepository.save(product1));
    }

    @Override
    public ResponseEntity<?> deleteProduct(Long id) {
        if(productRepository.findById(id).isPresent()){
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> updateProduct(ProductDTO product, Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if(productOptional.isPresent()){
            Product product1 = productOptional.get();
            product1.setName(product.getName());
            product1.setDescription(product.getDescription());
            product1.setPrice(product.getPrice());
            product1.setStock(product.getStock());
            product1.setImagePath(product.getImagePath());
            product1.setCategory(product.getCategory());
            product1.setModified(new Date());
            return  ResponseEntity.ok( productRepository.save(product1));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        Iterable<Product> productIterable = productRepository.findAll();
        List<Product> productList = new ArrayList<>();
        productIterable.forEach(productList::add);  // Añade todos los productos a la lista
        return ResponseEntity.ok(productList);
    }

    @Override
    public ResponseEntity<?> getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product!=null){
            return  ResponseEntity.ok( product);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> getProductByName(String name) {
        Product product = productRepository.findByName(name).orElse(null);
        if(product!=null){
            return  ResponseEntity.ok( product);
        }

        return ResponseEntity.notFound().build();
    }
}
