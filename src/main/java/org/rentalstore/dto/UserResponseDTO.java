package org.rentalstore.dto;

import lombok.Data;

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


}
