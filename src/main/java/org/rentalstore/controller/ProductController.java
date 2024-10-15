package org.rentalstore.controller;

import org.rentalstore.dto.ProdcutAdminDTO;
import org.rentalstore.dto.ProductDTO;
import org.rentalstore.entity.Product;
import org.rentalstore.service.imp.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api-product")
@CrossOrigin(origins = "http://localhost:5173/")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody final ProductDTO product) {
        return productService.saveProduct(product);

    }
    @GetMapping("/list")
    public ResponseEntity<List<ProdcutAdminDTO>> list() {
        return  productService.getProducts();
    }
    @GetMapping("/getById")
    public ResponseEntity<?> getById(@RequestParam final long id) {
        return productService.getProductById(id);
    }
    @GetMapping("/getByName")
    public ResponseEntity<?> getByName(@RequestParam String name) {
        return productService.getProductByName(name);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody final ProductDTO product, @RequestParam final long id) {
        return productService.updateProduct(product, id);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam final Long  id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/getListCategory")
    public ResponseEntity<List<Product>> getListCategory(@RequestParam String category){
        return productService.getProductsByCategory(category);
    }
}
