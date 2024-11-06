package org.rentalstore.dto.request;

import lombok.Data;

@Data
public class BuyCarDTO {
    private Double tax;
    private Double subTotal;
    private Double total;
}
