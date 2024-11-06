package org.rentalstore.dto.request;


import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private int rol;
}
