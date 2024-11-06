package org.rentalstore.controller;


import org.rentalstore.dto.request.LoginUserDTO;
import org.rentalstore.dto.request.UserDTO;
import org.rentalstore.service.imp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-user")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserController  {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
        return userService.saveUser(user);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO user) {
        return userService.login(user.getUserName(), user.getEmail(), user.getPassword());
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam int id) {
        return userService.getUser(id);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam int id) {
        return userService.deleteUser(id);
    }

    @PatchMapping("/updateRol")
    public ResponseEntity<?> updateRol(@RequestParam int id, @RequestParam String rol) {
        return userService.updateUser(id,rol);
    }

}
