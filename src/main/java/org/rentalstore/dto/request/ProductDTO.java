package org.rentalstore.dto.request;

import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private String description;
    private Double price;
    private Integer category;
    private int stock;
    private String imagePath;


}
