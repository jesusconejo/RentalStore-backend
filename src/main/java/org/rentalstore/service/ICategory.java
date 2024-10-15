package org.rentalstore.service;


import org.rentalstore.dto.CategoryDTO;
import org.rentalstore.entity.Category;
import org.springframework.http.ResponseEntity;

public interface ICategory {
    ResponseEntity<?> addCategory(CategoryDTO category);
    ResponseEntity<?> deleteCategory(int id);
    ResponseEntity<?> getAllCategories();
}
