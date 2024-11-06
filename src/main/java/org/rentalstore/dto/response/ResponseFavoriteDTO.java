package org.rentalstore.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseFavoriteDTO {
    private Long idUser;
    private Long idProduct;
    private int qualification;
    private Date created;

}
