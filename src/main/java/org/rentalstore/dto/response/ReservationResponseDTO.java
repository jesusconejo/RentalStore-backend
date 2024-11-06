package org.rentalstore.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationResponseDTO {
    private int idresrvation;
    private long productId;
    private long userId;
    private Date startReservation;
    private Date endReservation;
}
