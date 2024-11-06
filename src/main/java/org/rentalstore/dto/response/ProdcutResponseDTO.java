package org.rentalstore.dto.response;

import lombok.Data;

@Data
public class ProdcutResponseDTO {
    private int id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private int stock;
    private String imagePath;
    private String created;
    private String modified;
    private Integer favorite;

}
