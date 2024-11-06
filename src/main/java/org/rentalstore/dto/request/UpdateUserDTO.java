package org.rentalstore.dto.request;


import lombok.Data;

@Data
public class UpdateUserDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
