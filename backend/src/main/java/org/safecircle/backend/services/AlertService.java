package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.*;
import org.safecircle.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.safecircle.backend.dto.FcmTokenDTO;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.logging.Logger;

@Service
public class AlertService {
    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final UserService userService;
    private final CircleUserRepository circleUserRepository;
    private final CircleService circleService;
    private final AlertRepository alertRepository;
    private final LocationRepository locationRepository;
    private final CircleRepository circleRepository;
    private final CircleAlertRepository circleAlertRepository;
    private FcmTokenDTO fcmTokenDTO;
    private FcmToken fcmToken;

    @Autowired
    public AlertService(
            UserRepository userRepository,
            LocationService locationService,
            FcmTokenRepository fcmTokenRepository,
            UserService userService,
            CircleUserRepository circleUserRepository,
            CircleService circleService,
            AlertRepository alertRepository,
            LocationRepository locationRepository,
            CircleRepository circleRepository,
            CircleAlertRepository circleAlertRepository
    ) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.fcmTokenRepository = fcmTokenRepository;
        this.userService = userService;
        this.circleUserRepository = circleUserRepository;
        this.circleService = circleService;
        this.alertRepository = alertRepository;
        this.locationRepository = locationRepository;
        this.circleRepository = circleRepository;
        this.circleAlertRepository = circleAlertRepository;
    }

    public void sendNotification(String token, String alertType, String description, BigDecimal latitude, BigDecimal longitude, long userId) {
        RestTemplate restTemplate = new RestTemplate();

        // Create JSON payload
        String payload = String.format(
                "{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\",\"data\":{\"latitude\":\"%s\",\"longitude\":\"%s\"}}",
                token, alertType, description, latitude, longitude
        );

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Create HTTP request
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(EXPO_PUSH_URL, request, String.class);
            System.out.println("Notification sent to token: " + token + ". Response: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Failed to send notification to token: " + token);
            e.printStackTrace();
        }
    }

    public ResponseEntity<String> sendAlert(long userId, AlertDTO alert) {
        if(!userService.isUserValid(alert.getUserId())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserService.USER_NOT_FOUND);
        }

        if(alert.getStatus().equals(SafetyStatus.UNSAFE)){
            return sendUnsafeAlert(userId, alert);
        }else{
            return sendSOSAlert(userId, alert);
        }
    }

    public ResponseEntity<String> sendUnsafeAlert(long userid, AlertDTO alert) {
        User user = userService.getUserById(alert.getUserId());

        if (!circleService.isUserInCircles(alert.getUserId(), alert.getCircles())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not in circles he requested to send to");
        }

        List<User> usersInCircle = new ArrayList<>();
        for (Long circleId : alert.getCircles()) {
            if (!circleService.isCircleValid(circleId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Circle is not valid");
            }
            Circle circle = circleService.getCircleById(circleId);
            List<CircleUser> circleUsers = circleUserRepository.findByCircle(circle);
            circleUsers.forEach(circleUser -> usersInCircle.add(circleUser.getUser()));
        }

        Location alertLocation = new Location(alert.getLocation().latitude(), alert.getLocation().longitude());
        locationRepository.save(alertLocation);
        Alert alertSave = new Alert(alert.getStatus(),alert.getDescription(),alertLocation, user,alert.getDuration(), alert.setActive(true));
        alertRepository.save(alertSave);

        for (long circleId : alert.getCircles()) {
          Circle circle =  circleRepository.save(circleService.getCircleById(circleId));
            CircleAlert circleAlert = new CircleAlert(alertSave,circle );
            circleAlertRepository.save(circleAlert);
        }

        Set<Long> notifiedUserIds = Collections.synchronizedSet(new HashSet<>());

        usersInCircle.parallelStream().forEach(userInCircle -> {
            if (notifiedUserIds.contains(userInCircle.getUserId())) {
                return;

            }
            List<FcmToken> tokens = fcmTokenRepository.findByUser(userInCircle);
            if (!tokens.isEmpty()) {
                FcmToken token = tokens.getFirst();
                sendNotification(
                        token.getFcmToken(),
                        "Unsafe Alert: " + user.getFirstName() + " " + user.getLastName(),
                        alert.getDescription(),
                        alert.getLocation().latitude(),
                        alert.getLocation().longitude(),
                        alert.getUserId()
                );
                notifiedUserIds.add(userInCircle.getUserId());
            }

        }
        );

        return ResponseEntity.status(HttpStatus.OK).body("Unsafe Alert: " + alert.getStatus());
    }

    public ResponseEntity<String> stopAlert(long UserId) {
        User user = userService.getUserById(UserId);
        Alert activeAlert = getActiveAlert(user.getUserId());

       activeAlert.setIsactive(false);
       activeAlert.setDurationOfAlert(Duration.between(activeAlert.getCreatedAt(), activeAlert.getUpdatedAt()));
       alertRepository.save(activeAlert);
        return ResponseEntity.status(HttpStatus.OK).body("Alert Stopped");
    }

    public ResponseEntity<String> sendSOSAlert(long userId, AlertDTO alert) {
        User user = userService.getUserById(alert.getUserId());
        List<User> users = userRepository.findAllByLocationIsNotNull();

        Location alertLocation = new Location(alert.getLocation().latitude(), alert.getLocation().longitude());
        locationRepository.save(alertLocation);
        Alert alertSave = new Alert(alert.getStatus(),alert.getDescription(),alertLocation, user, alert.getDuration(), alert.setActive(true));
        alertRepository.save(alertSave);

        Set<Long> notifiedUserIds = new HashSet<>();

        for (User userInArea : users) {
            BigDecimal distance = locationService.calculateDistance(
                    alert.getLocation().latitude(),
                    alert.getLocation().longitude(),
                    userInArea.getLocation().getLatitude(),
                    userInArea.getLocation().getLongitude()
            );

            BigDecimal maxDistance = new BigDecimal("2.0");
            if (distance.compareTo(maxDistance) <= 0) {
                if (notifiedUserIds.contains(userInArea.getUserId())) {
                    continue;
                }
                List<FcmToken> tokens = fcmTokenRepository.findByUser(userInArea);
                FcmToken token = tokens.get(0);
                    sendNotification(
                            token.getFcmToken(),
                            "SOS Alert: " + alert.getStatus(),
                            alert.getDescription(),
                            alert.getLocation().latitude(),
                            alert.getLocation().longitude(),
                            userId
                    );
                    notifiedUserIds.add(userInArea.getUserId());

            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("SOS Alert: " + alert.getStatus());
    }

        public Alert getActiveAlert(long userId) {
        User user = userService.getUserById(userId);
        List<Alert> alerts = alertRepository.findByUser(user);


        for (Alert alert : alerts) {
            List<Alert> activeAlerts = alertRepository.findByAlertIsActive(true);

            if (alert.getStatus().equals(SafetyStatus.UNSAFE)) {
               CircleAlert circleAlert = alert.getCircleAlerts().iterator().next();
                if (circleService.isUserInCircle(circleAlert.getCircle().getCircleId(), userId)){
                    return activeAlerts.getFirst();
                }
            }

            if (alert.getStatus().equals(SafetyStatus.SOS)) {
                return activeAlerts.getFirst();
            }
        }
        return null;
    }

}
