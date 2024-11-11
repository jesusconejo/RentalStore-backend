package org.rentalstore.dto.response;

import lombok.Data;
import org.rentalstore.entity.Product;

import java.util.Date;

@Data
public class ResponseLikeDTO {
    private int id;
    private long productId;
    private long userId;
    private Date date;
}
