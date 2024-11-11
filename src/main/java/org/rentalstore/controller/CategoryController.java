package org.rentalstore.controller;

import org.rentalstore.dto.request.CategoryDTO;
import org.rentalstore.service.imp.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-category")
@CrossOrigin( origins = "http://localhost:5173/")
public class CategoryController {

    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO category) {
        return categoryService.addCategory(category);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCategory(@RequestParam int id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategory() {
        return categoryService.getAllCategories();
    }
}
