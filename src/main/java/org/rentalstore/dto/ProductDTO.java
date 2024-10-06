package org.rentalstore.dto;

import lombok.Data;
import org.rentalstore.entity.Category;

@Data
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private int stock;
    private String imagePath;
    private Category category;

}
