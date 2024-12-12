package org.safecircle.backend.services;

import com.nimbusds.jose.JOSEException;
import org.safecircle.backend.dto.AuthDTO;
import org.safecircle.backend.dto.UserDTO;
import org.safecircle.backend.config.JwtService;
import org.safecircle.backend.enums.UserType;
import org.safecircle.backend.models.CircleUser;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.safecircle.backend.models.UserAlert;
import org.safecircle.backend.repositories.CircleUserRepository;
import org.safecircle.backend.repositories.LocationRepository;
import org.safecircle.backend.repositories.UserAlertRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class UserService {

    public static final String USER_CREATED = "User created";
    public static final String USER_UPDATED = "User updated";
    public static final String USER_DELETED = "User deleted";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ALREADY_EXISTS = "User already exists";
    public static final String USER_LOGGED_IN = "Logged in";
    public static final String USER_LOGGED_OUT = "Logged out";
    public static final String WRONG_CREDENTIALS = "Wrong credentials";
    public static final String NOT_AUTHORIZED = "Not authorized";

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    private UserRepository userRepository;
    private UserAlertRepository userAlertRepository;
    private CircleUserRepository circleUserRepository;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setUserAlertRepository(UserAlertRepository userAlertRepository) {
        this.userAlertRepository = userAlertRepository;
    }

    @Autowired
    public void setCircleUserRepository(CircleUserRepository circleUserRepository) {
        this.circleUserRepository = circleUserRepository;
    }

    public ResponseEntity<String> createUser(UserDTO userDTO) {
        try{
            if(userRepository.existsByEmail(userDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ALREADY_EXISTS);
            }


            User user = new User(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    bCryptPasswordEncoder.encode(userDTO.getPassword()),
                    userDTO.getPhone(),
                    UserType.USER
                    );


            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(USER_CREATED);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error processing your request: " + e.getMessage());
        }
    }

    public ResponseEntity<String> authenticateUser(AuthDTO authDTO) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(authDTO.getEmail()).stream().findFirst();
            if (userOpt.isEmpty() || !bCryptPasswordEncoder.matches(authDTO.getPassword(), userOpt.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(WRONG_CREDENTIALS);
            }
            String token = jwtService.generateToken(userOpt.get().getUserId(), userOpt.get().getType());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }catch (JOSEException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error has occurred during authentication");
        }
    }

    public ResponseEntity<String> updateUser(long userId, String firstName, String lastName, String email,String phoneNumber, String password) {

        boolean isChanged = false;

        if (!isUserValid(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        }
        try {
            User user = getUserById(userId);

            if(firstName != null && !firstName.isEmpty()  && (lastName.trim().length() >= 2 && lastName.trim().length() <= 40)){
                user.setFirstName(firstName.trim());
                isChanged = true;
            }
            if(lastName != null && !lastName.isEmpty() && (lastName.trim().length() >= 2 && lastName.trim().length() <= 50)){
                user.setLastName(lastName.trim());
                isChanged = true;
            }
            if(email != null && !email.isEmpty()){
                if(isValidEmail(email)){
                    user.setEmail(email.trim());
                    isChanged = true;
                }
            }
            if(phoneNumber != null && !phoneNumber.isEmpty()){
                user.setPhoneNumber(phoneNumber.trim());
                isChanged = true;
            }
            if(password != null && !password.isEmpty() && (password.length()> 8 && password.length() < 40 )){
                user.setPassword(bCryptPasswordEncoder.encode(password));
                isChanged = true;
            }

            if(isChanged){
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(USER_UPDATED);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nothing to update");
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured: could not update user");
        }
    }

    public ResponseEntity<String> updateRole(UserType type, long userId, UserType role) {
        if(!isUserValid(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        }
        if(!isAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(NOT_AUTHORIZED);
        }
        User user = getUserById(userId);
        user.setType(role);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(USER_UPDATED);
    }

    public ResponseEntity<String> updateLocation(long userId, double latitude, double longitude) {
        try {
            if (!isUserValid(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
            }
            User user = getUserById(userId);
            Location location = user.getLocation();
            location.setLatitude(BigDecimal.valueOf(latitude));
            location.setLongitude(BigDecimal.valueOf(longitude));

            locationRepository.save(location);
            return ResponseEntity.status(HttpStatus.OK).body(USER_UPDATED);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured: could not update user location");
        }
    }

    public ResponseEntity<String> deleteUser(long userId) {
        if(!isUserValid(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(USER_NOT_FOUND);
        }
        User user = getUserById(userId);
        List<UserAlert> userAlerts = userAlertRepository.findByUser(user);
        List<CircleUser> circleUsers = circleUserRepository.findByUser(user);

        userAlertRepository.deleteAll(userAlerts);
        circleUserRepository.deleteAll(circleUsers);
        userRepository.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body(USER_DELETED);
    }

    public User getUserById(long userId){
            return userRepository.findById(userId).orElse(null);
    }

    public List<User> getUserByFirstNameContaining(String firstName){
        return userRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    public List<User> getUserByLastNameContaining(String lastName){
        return userRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public List<User> getUserByFirstnameAndLastnameContaining(String firstName, String lastName){
        Set<User> users = new HashSet<>();
        users.addAll(userRepository.findByFirstNameContainingIgnoreCase(firstName));
        users.addAll(userRepository.findByLastNameContainingIgnoreCase(lastName));
        return new ArrayList<>(users);
    }

    public boolean isUserValid(long userId){
        return userRepository.existsByUserId(userId);
    }

    public boolean isUserValidByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean isAdmin(long userId){
        if(!userRepository.existsByUserId(userId)){
            return false;
        }
        UserType role = getUserById(userId).getType();
        return UserType.ADMIN.equals(role);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }


}
