package org.rentalstore.dto.response;

import lombok.Data;
import org.rentalstore.entity.Product;

import java.util.List;

@Data
public class BuyCarResponse {
    private Double tax;
    private Double price;
    private Double subTotal;
    private Double total;
    private List<Product> products;
}
