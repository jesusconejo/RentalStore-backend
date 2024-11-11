package org.rentalstore.service.imp;

import org.rentalstore.dto.request.CategoryDTO;
import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.entity.Category;
import org.rentalstore.entity.Product;
import org.rentalstore.repository.CategoryRepository;
import org.rentalstore.repository.ProductRepository;
import org.rentalstore.service.ICategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategory {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ResponseEntity<?> addCategory(CategoryDTO category) {
        Category categoryEntity = new Category();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        Optional<Category> category1 = categoryRepository.findByName(category.getName());
        if (category1.isPresent()) {
            errorResponseDTO.setMessage("name Category exist");
            errorResponseDTO.setErrorCode(9);
            errorResponseDTO.setError("duplicate");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
        }
        categoryEntity.setName(category.getName().toUpperCase());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setCreated(new Date());
        return new ResponseEntity<>(categoryRepository.save(categoryEntity), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> deleteCategory(int id) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        Optional<Category> category1 = categoryRepository.findById(id);
        if (category1.isPresent()) {
            List<Product> productOptional = productRepository.findByCategory(category1.get());
            if(productOptional.isEmpty()) {
                categoryRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).build();
            }else {
                errorResponseDTO.setMessage("category is looked by product");
                errorResponseDTO.setErrorCode(9);
                errorResponseDTO.setError("duplicate");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
            }

        }
        errorResponseDTO.setMessage("category not found");
        errorResponseDTO.setErrorCode(9);
        errorResponseDTO.setError("category not found");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @Override
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryRepository.findAll());
    }
}
