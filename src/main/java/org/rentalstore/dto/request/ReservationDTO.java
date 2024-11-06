package org.rentalstore.dto.request;

import lombok.Data;

@Data
public class ReservationDTO {
    private int idUser;
    private long idProduct;
    private String startReservation;
    private String endReservation;
}
