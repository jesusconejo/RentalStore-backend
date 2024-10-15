package org.rentalstore.service.imp;

import org.rentalstore.dto.ProdcutAdminDTO;
import org.rentalstore.dto.ProductDTO;
import org.rentalstore.entity.Product;
import org.rentalstore.repository.CategoryRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.service.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductService implements IProduct {

    private ProductRepository productRepository;

    //private CategoryMapper categoryMapper;
     private CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository repo, CategoryRepository catRepo) {
        this.categoryRepository= catRepo;
        this.productRepository = repo;
    }

    @Override
    public ResponseEntity<?> saveProduct(ProductDTO product) {
       if(productRepository.findByName(product.getName()).isPresent()){
           return new ResponseEntity<>("Product already exists", HttpStatus.BAD_REQUEST);
       }
        Product product1 = new Product();
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        product1.setPrice(product.getPrice());
        product1.setImagePath(product.getImagePath());
        product1.setCreated(new Date());
        product1.setStock(product.getStock());
        product1.setCategory(categoryRepository.findById(Integer.valueOf(product.getCategory())).get());
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

            Optional<Product> productWithName = productRepository.findByName(product.getName());
            if(productWithName.isPresent() && !productWithName.get().getId().equals(id)){
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
            return  ResponseEntity.ok( productRepository.save(product1));
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ProdcutAdminDTO>> getProducts() {
        Iterable<Product> productIterable = productRepository.findAll();
        List<ProdcutAdminDTO> productList = new ArrayList<>();

        for (Product product : productIterable) {
            // Crear un objeto DTO y mapear los valores desde Product
            ProdcutAdminDTO productDTO = new ProdcutAdminDTO();
            productDTO.setId(product.getId().intValue()); // Si quieres el id como int
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setStock(product.getStock());
            productDTO.setImagePath(product.getImagePath());

            // Convertir categoría de Integer a String usando tu método o HashMap

            productDTO.setCategory(product.getCategory().getName());

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

        return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Product>> getProductsByCategory(String categoryValue) {

        List<Product> productList = productRepository.findByCategory(categoryRepository.findById(Integer.valueOf(categoryValue)).get());
        return ResponseEntity.ok(productList);
    }

    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }


}
