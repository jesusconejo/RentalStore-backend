package org.rentalstore.dto.response;

import lombok.Data;
import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.entity.BuyCar;

import java.util.Set;

@Data
public class UserResponseDTO {
   private ErrorResponseDTO errorResponseDTO;
    private Long id;
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String rol;
    private String created;
    private String modified;
    private Set<ResponseLikeDTO> likeList;
    private Set<ResponseFavoriteDTO> favoriteList;
    private Set<ReservationResponseDTO> reservationList;
    private int buyCarId;


}
