package org.rentalstore.service;

import org.rentalstore.dto.request.UserDTO;
import org.springframework.http.ResponseEntity;

public interface IUser {

    ResponseEntity<?> saveUser(UserDTO user);
    ResponseEntity<?> deleteUser(int id);
    ResponseEntity<?> updateUser(int id, String rol);
    ResponseEntity<?> getUser(int id);
    ResponseEntity<?> getUsers();

    ResponseEntity<?> login(String username,String email,  String password);
    ResponseEntity<?> logout();
    ResponseEntity<?> register(String username, String password);
    ResponseEntity<?> changePassword(String username, String oldPassword, String newPassword);
    ResponseEntity<?> changeEmail(String username, String email);

}
