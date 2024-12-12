package org.safecircle.backend.controllers;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.safecircle.backend.dto.UserDTO;
import org.safecircle.backend.dto.AuthDTO;
import org.safecircle.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin
    @PostMapping(value = "/user/create")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDTO user){
        return userService.createUser(user);
    }

    @CrossOrigin
    @PostMapping(value = "/user/authenticate")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody AuthDTO authDTO) {
            return userService.authenticateUser(authDTO);
    }
}
