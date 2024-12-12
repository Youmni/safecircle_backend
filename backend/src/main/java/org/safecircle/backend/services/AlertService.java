package org.safecircle.backend.services;

import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.User;
import org.safecircle.backend.models.UserAlert;
import org.safecircle.backend.models.UserAlertKey;
import org.safecircle.backend.repositories.UserAlertRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final UserService userService;
    private final UserAlertRepository userAlertRepository;

    public AlertService(UserService userService, UserAlertRepository userAlertRepository) {
        this.userService = userService;
        this.userAlertRepository = userAlertRepository;
    }

    public ResponseEntity<String> createAlert(AlertDTO alertDto) {
        try{

            if(userService.isUserValid(alertDto.getUserId())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            User user = userService.getUserById(alertDto.getUserId());

            Alert alert = new Alert(
                alertDto.getStatus(),
                alertDto.getDescription(),
                alertDto.getLocation()
            );

            UserAlert userAlert = new UserAlert(
                    user,
                    alert
            );
            UserAlertKey key = new UserAlertKey(
                    user.getUserId(), alert.getAlertId()
            );
            userAlert.setId(key);
            userAlertRepository.save(userAlert);

            return ResponseEntity.status(HttpStatus.CREATED).body("Alert created for user");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
//    public ResponseEntity<String> stopAlert(long userId, long alertId) {
//
//    }
}
