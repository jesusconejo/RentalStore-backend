package org.rentalstore.dto;

import lombok.Data;

@Data
public class ProdcutAdminDTO extends ProductDTO{
    private int id;
    private String created;
    private String modified;

}
