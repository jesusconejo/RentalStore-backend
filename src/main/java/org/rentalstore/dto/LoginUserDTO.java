package org.rentalstore.dto;

import lombok.Data;

@Data
public class LoginUserDTO {
    private String userName;
    private String email;
    private String password;

}
