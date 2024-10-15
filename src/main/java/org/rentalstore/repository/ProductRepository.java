package org.rentalstore.repository;

import org.rentalstore.entity.Category;
import org.rentalstore.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

    Optional<Product> findByName(String productName);
    List<Product> findByCategory(Category category);
}
