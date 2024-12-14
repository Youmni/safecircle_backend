package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.*;
import org.safecircle.backend.repositories.CircleUserRepository;
import org.safecircle.backend.repositories.FcmTokenRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.safecircle.backend.dto.FcmTokenDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Service
public class AlertService {
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final UserService userService;
    private final CircleUserRepository circleUserRepository;
    private final CircleService circleService;
    private FcmTokenDTO fcmTokenDTO;
    private FcmToken fcmToken;

    @Autowired
    public AlertService(UserRepository userRepository, LocationService locationService, FcmTokenRepository fcmTokenRepository, UserService userService, CircleUserRepository circleUserRepository, CircleService circleService) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.fcmTokenRepository = fcmTokenRepository;
        this.userService = userService;
        this.circleUserRepository = circleUserRepository;
        this.circleService = circleService;
    }

    public void sendNotification(String title, String body, BigDecimal latitude, BigDecimal longitude, String token) {
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("latitude", String.valueOf(latitude))
                    .putData("longitude", String.valueOf(longitude))
                    .setToken(token)
                    .build();

            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<String> sendAlert(AlertDTO alert) {
        if(!userService.isUserValid(alert.getUserId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserService.USER_NOT_FOUND);
        }

        if(alert.getStatus().equals(SafetyStatus.UNSAFE)){
            return sendUnsafeAlert(alert);
        }else{
            return sendSOSAlert(alert);
        }
    }

    public ResponseEntity<String> sendUnsafeAlert(AlertDTO alert) {
        User user = userService.getUserById(alert.getUserId());

        if(!circleService.isUserInCircles(alert.getUserId(), alert.getCircles())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not in circles he requested to send to");
        }

        List<User> usersInCircle = new ArrayList<>();

        for(Long cirlceId : alert.getCircles()){
            if(!circleService.isCircleValid(cirlceId)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Circle is not valid");
            }
            Circle circle = circleService.getCircleById(cirlceId);
            List<CircleUser> circleUsers = circleUserRepository.findByCircle(circle);
            circleUsers.forEach(circleUser -> usersInCircle.add(circleUser.getUser()));

        }

        for(User userInd : usersInCircle){
            List<FcmToken> tokens = fcmTokenRepository.findByUser(userInd);
            if (!tokens.isEmpty()) {
                for(FcmToken token : tokens){
                    User unsafeUser = userService.getUserById(alert.getUserId());
                    sendNotification(
                            "Unsafe Alert: " + unsafeUser.getFirstName() + " " + unsafeUser.getLastName(),
                            alert.getDescription(),
                            alert.getLocation().latitude(),
                            alert.getLocation().longitude(),
                            token.getFcmToken()
                    );
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("Unsafe Alert: " + alert.getStatus());
    }

    public ResponseEntity<String> sendSOSAlert(AlertDTO alert) {
        List<User> users = userRepository.findAllByLocationIsNotNull();
        for (User userInd : users) {
            BigDecimal distance = locationService.calculateDistance(
                    alert.getLocation().latitude(),
                    alert.getLocation().longitude(),
                    userInd.getLocation().getLatitude(),
                    userInd.getLocation().getLongitude()
            );
            User user = userService.getUserById(alert.getUserId());
            BigDecimal maxDistance = new BigDecimal("2.0");
            if (distance.compareTo(maxDistance) <= 0 && fcmTokenRepository.findByUserAndFcmTokenId(user, fcmToken.getFcmTokenId()) != null) {
                sendNotification(
                        "Emergency Alert: " + alert.getStatus(),
                        alert.getDescription(),
                        alert.getLocation().latitude(),
                        alert.getLocation().longitude(),
                        fcmTokenDTO.getFcmToken()
                );
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("SOS Alert: " + alert.getStatus());
    }
}
