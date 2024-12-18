package org.safecircle.backend.controllers;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.safecircle.backend.dto.FcmTokenDTO;
import org.safecircle.backend.dto.AuthDTO;
import org.safecircle.backend.dto.RefreshTokenRequest;
import org.safecircle.backend.dto.UserDTO;
import org.safecircle.backend.models.User;
import org.safecircle.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDTO user){
        return userService.createUser(user);
    }

    @CrossOrigin
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthDTO authDTO) {
            return userService.authenticateUser(authDTO);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        return userService.refreshToken(request);
    }

//    @CrossOrigin
//    @PostMapping(value = "/authenticate")
//    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthDTO authDTO) {
//        return userService.authenticateUser(authDTO);
//    }

    @CrossOrigin
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable long userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String password
    ) {
        try {
            ResponseEntity<String> response = userService.updateUser(userId, firstName, lastName, email, phoneNumber, password);
            return response;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred while updating the user");
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{userId}")
    public ResponseEntity<User> getUser(@PathVariable long userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @CrossOrigin
    @GetMapping("/search/first-name")
    public ResponseEntity<List<User>> getUsersByFirstName(@RequestParam String firstName) {
        List<User> users = userService.getUserByFirstNameContaining(firstName);
        return ResponseEntity.ok(users);
    }
    @CrossOrigin
    @GetMapping("/search/last-name")
    public ResponseEntity<List<User>> getUsersByLastName(@RequestParam String lastName) {
        List<User> users = userService.getUserByLastNameContaining(lastName);
        return ResponseEntity.ok(users);
    }

    @CrossOrigin
    @GetMapping("/search")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName) {
        if (firstName == null || lastName == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<User> users = userService.getUserByFirstnameAndLastnameContaining(firstName, lastName);
        return ResponseEntity.ok(users);
    }
    @CrossOrigin
    @PostMapping("/{id}/register-token")
    public ResponseEntity<String> registerFcmTokens(@PathVariable long id, @RequestBody FcmTokenDTO fcmTokenDTO) {
        return userService.registerFcmToken(id, fcmTokenDTO);
    }

    @PutMapping("/location/{userId}")
    public ResponseEntity<String> updateUserLocation(
            @PathVariable long userId,
            @RequestParam double latitude,
            @RequestParam double longitude
    ) {
        return userService.updateLocation(userId, latitude, longitude);
    }
}
