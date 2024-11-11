package org.rentalstore.dto.response;

import lombok.Data;
import org.rentalstore.entity.BuyCar;

import java.util.Date;

@Data
public class ReservationResponseDTO {
    private int idresrvation;
    private long productId;
    private long userId;
    private BuyCarResponse buyCar;
    private Date startReservation;
    private Date endReservation;
}
