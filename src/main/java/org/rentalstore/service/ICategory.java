package org.rentalstore.service;


import org.rentalstore.dto.request.CategoryDTO;
import org.springframework.http.ResponseEntity;

public interface ICategory {
    ResponseEntity<?> addCategory(CategoryDTO category);
    ResponseEntity<?> deleteCategory(int id);
    ResponseEntity<?> getAllCategories();
}
