package org.rentalstore.service.imp;

import org.rentalstore.dto.ErrorResponseDTO;
import org.rentalstore.dto.UserDTO;
import org.rentalstore.dto.UserResponseDTO;
import org.rentalstore.entity.Role;
import org.rentalstore.entity.User;
import org.rentalstore.repository.UserRepository;
import org.rentalstore.service.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUser {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Role role;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        role = new Role();
    }

    @Override
    public ResponseEntity<?> saveUser(UserDTO userDto) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        LOGGER.info("ROL: ", userDto.getRol());
        if (userRepository.findByUsername(userDto.getUserName()).isPresent()) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
            errorResponseDTO.setErrorCode(9);
            errorResponseDTO.setError("userName");
            errorResponseDTO.setMessage(userDto.getUserName());
            responseDTO.setErrorResponseDTO(errorResponseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
            errorResponseDTO.setErrorCode(9);
            errorResponseDTO.setError("email");
            errorResponseDTO.setMessage(userDto.getEmail());
            responseDTO.setErrorResponseDTO(errorResponseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUserName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        if (userDto.getRol() > 0){
            user.setRole(Role.getRolById(userDto.getRol()));
        }else{
            user.setRole(Role.getRolById(2));
        }

        user.setEmail(userDto.getEmail());
        user.setCreated(new Date());
        try {

            User userOptional = userRepository.save(user);
            responseDTO.setId(userOptional.getId());
            responseDTO.setEmail(userOptional.getEmail());
            responseDTO.setName(userOptional.getName());
            responseDTO.setLastName(userOptional.getLastName());
            responseDTO.setUserName(userOptional.getUsername());
            responseDTO.setRol(userOptional.getRole());
            responseDTO.setCreated(userOptional.getCreated().toString());
            return ResponseEntity.ok(responseDTO);

        } catch (Exception e) {
            LOGGER.error("Error saving user", e);
            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
            errorResponseDTO.setErrorCode(5);
            errorResponseDTO.setError("internal server error");
            errorResponseDTO.setMessage(userDto.getEmail());
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> deleteUser(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<?> updateUser(int id, String rol) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            UserResponseDTO responseDTO = new UserResponseDTO();
            User user = userOptional.get();
            user.setRole(rol);
            user.setModified(new Date());
            user =userRepository.save(user);
            responseDTO.setId(user.getId());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setName(user.getName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setRol(user.getRole());
            responseDTO.setCreated(user.getCreated().toString());
            responseDTO.setModified(user.getModified().toString());

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Override
    public ResponseEntity<?> getUser(int id) {
        Optional<User> userOptional = Optional.empty();
        UserResponseDTO responseDTO = new UserResponseDTO();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            responseDTO.setId(userOptional.get().getId());
            responseDTO.setEmail(userOptional.get().getEmail());
            responseDTO.setName(userOptional.get().getName());
            responseDTO.setLastName(userOptional.get().getLastName());
            responseDTO.setUserName(userOptional.get().getUsername());
            responseDTO.setCreated(userOptional.get().getCreated().toString());
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        } else {
            errorResponseDTO.setErrorCode(4);
            errorResponseDTO.setError("id");
            errorResponseDTO.setMessage("id not found");
            responseDTO.setErrorResponseDTO(errorResponseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<?> getUsers() {
        List<UserResponseDTO> responseDTOs = new ArrayList<>();
        Iterable<User> userList = userRepository.findAll();
        for (User user : userList) {
            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setEmail(user.getEmail());
            responseDTO.setName(user.getName());
            responseDTO.setLastName(user.getLastName());
            responseDTO.setUserName(user.getUsername());
            responseDTO.setRol(user.getRole());
            responseDTO.setCreated(user.getCreated().toString());
            responseDTO.setModified(user.getModified()!=null?user.getModified().toString():null);
            responseDTOs.add(responseDTO);
        }
        return new ResponseEntity<>(responseDTOs, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> login(String username, String email, String password) {
        Optional<User> userOptional = Optional.empty();
        UserResponseDTO responseDTO = new UserResponseDTO();
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO();
        if (username != null) {
            userOptional = userRepository.findByUsername(username);
            errorResponseDTO.setError("userName");
            errorResponseDTO.setMessage(username);
        } else if (email != null) {
            userOptional = userRepository.findByEmail(email);
            errorResponseDTO.setError("email");
            errorResponseDTO.setMessage(email);
        }
        if (userOptional.isEmpty()) {
            errorResponseDTO.setErrorCode(4);
            responseDTO.setErrorResponseDTO(errorResponseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            errorResponseDTO.setErrorCode(1);
            errorResponseDTO.setError("UNAUTHORIZED");
            errorResponseDTO.setMessage("no authorized");
            responseDTO.setErrorResponseDTO(errorResponseDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.UNAUTHORIZED);
        }


        responseDTO.setId(user.getId());
        responseDTO.setUserName(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setName(user.getName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setRol(user.getRole());
        responseDTO.setCreated(user.getCreated().toString());

        return ResponseEntity.ok(responseDTO);
    }


    @Override
    public ResponseEntity<?> logout() {

        return null;
    }

    @Override
    public ResponseEntity<?> register(String username, String password) {

        return null;
    }

    @Override
    public ResponseEntity<?> changePassword(String username, String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public ResponseEntity<?> changeEmail(String username, String email) {
        return null;
    }

    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
