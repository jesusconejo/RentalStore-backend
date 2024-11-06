package org.rentalstore.service.imp;

import org.rentalstore.dto.error.ErrorResponseDTO;
import org.rentalstore.dto.request.UserDTO;
import org.rentalstore.dto.response.ReservationResponseDTO;
import org.rentalstore.dto.response.ResponseFavoriteDTO;
import org.rentalstore.dto.response.ResponseLikeDTO;
import org.rentalstore.dto.response.UserResponseDTO;
import org.rentalstore.entity.*;
import org.rentalstore.repository.*;
import org.rentalstore.service.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService implements IUser {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final BuyCarRepository buyCarRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReservationRepository reservationRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Role role;

    @Autowired
    public UserService(final UserRepository userRepository, LikeRepository likeRepository, BuyCarRepository buyCarRepository, FavoriteRepository favoriteRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.buyCarRepository = buyCarRepository;
        this.favoriteRepository = favoriteRepository;
        this.reservationRepository = reservationRepository;
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
            BuyCar buyCar = new BuyCar();
            buyCar.setUser(userOptional);
            BuyCar buyCar1= buyCarRepository.save(buyCar);


            Optional<User> user2= userRepository.findById(Math.toIntExact(userOptional.getId()));
          //  user2.get().setBuyCar(buyCar1.getUser().getBuyCar());
            userRepository.save(user2.get());
            responseDTO.setId(user2.get().getId());
            responseDTO.setEmail(user2.get().getEmail());
            responseDTO.setName(user2.get().getName());
            responseDTO.setLastName(user2.get().getLastName());
            responseDTO.setUserName(user2.get().getUsername());
            responseDTO.setRol(user2.get().getRole());
            responseDTO.setCreated(user2.get().getCreated().toString());
            //responseDTO.setBuyCarId(user2.get().getBuyCar().getId());
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
        ResponseLikeDTO responseLikeDTO ;
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
        Set<Like> likes = likeRepository.findAllByUser(userOptional);
        Set<Favorite> favorites = favoriteRepository.findAllByUser(userOptional);
        Set<Reservation> reservations = reservationRepository.findByUser(userOptional);
        Set<ResponseLikeDTO> responseLikeDTOs = new HashSet<>();
        Set<ResponseFavoriteDTO> responseFavoriteDTOs = new HashSet<>();
        Set<ReservationResponseDTO> reservationResponseDTOs = new HashSet<>();
        for (Like like : likes) {
            responseLikeDTO= new ResponseLikeDTO();
            responseLikeDTO.setId(like.getId());
            responseLikeDTO.setUserId(like.getUser().getId());
            responseLikeDTO.setProductId(like.getProduct().getId());
            responseLikeDTO.setDate(like.getLikeDate());
            responseLikeDTOs.add(responseLikeDTO);
        }
        for (Favorite favorite : favorites) {
            ResponseFavoriteDTO responseFavoriteDTO = new ResponseFavoriteDTO();
            responseFavoriteDTO.setIdUser(favorite.getUser().getId());
            responseFavoriteDTO.setIdProduct(favorite.getProduct().getId());
            responseFavoriteDTO.setQualification(favorite.getQuantity());
            responseFavoriteDTO.setCreated(favorite.getCreated());
            responseFavoriteDTOs.add(responseFavoriteDTO);
        }
        for(Reservation reservation : reservations ){
            ReservationResponseDTO responseReservationDTO = new ReservationResponseDTO();
            responseReservationDTO.setUserId(reservation.getId());
            responseReservationDTO.setProductId(reservation.getProduct().getId());
            responseReservationDTO.setStartReservation(reservation.getStart());
            responseReservationDTO.setEndReservation(plusDays(reservation.getStart(),7));
            reservationResponseDTOs.add(responseReservationDTO);
        }
        responseDTO.setId(user.getId());
        responseDTO.setUserName(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setName(user.getName());
        responseDTO.setLastName(user.getLastName());
        responseDTO.setRol(user.getRole());
        responseDTO.setCreated(user.getCreated().toString());
        responseDTO.setLikeList(responseLikeDTOs);
        responseDTO.setFavoriteList(responseFavoriteDTOs);
        responseDTO.setReservationList(reservationResponseDTOs);
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        return formatter.format(date);
    }
    private  Date plusDays(Date date, int days) {
        // Convertir Date a LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Restar los días
        LocalDate newDate = localDate.plusDays(days);

        // Convertir de nuevo LocalDate a Date
        return Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
