package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.safecircle.backend.dtos.*;
import org.safecircle.backend.dtos.ActiveAlertDTO;
import org.safecircle.backend.dtos.FcmTokenDTO;
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
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

    public void sendNotificationUnSafe(String token, String alertType, String description, BigDecimal latitude, BigDecimal longitude, long userId) {
        RestTemplate restTemplate = new RestTemplate();

        // Create JSON payload
        String payload = String.format(
                "{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\",\"data\":{\"latitude\":\"%s\",\"longitude\":\"%s\",\"userId\":\"%s\"}}",
                token, alertType, description, latitude, longitude, userId
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

    public void sendNotificationSOS(String token, String alertType, String description, BigDecimal latitude, BigDecimal longitude, long userId) {
        RestTemplate restTemplate = new RestTemplate();

        // Create JSON payload
        String payload = String.format(
                "{\"to\":\"%s\",\"title\":\"%s\",\"body\":\"%s\",\"data\":{\"latitude\":\"%s\",\"longitude\":\"%s\",\"userId\":\"%s\"}}",
                token, alertType, description, latitude, longitude, userId
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
        if(!userService.isUserValid(userId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(UserService.USER_NOT_FOUND);
        }

        if(alert.getStatus().equals(SafetyStatus.UNSAFE)){
            return sendUnsafeAlert(userId, alert);
        }else{
            return sendSOSAlert(userId, alert);
        }
    }

    public ResponseEntity<String> sendUnsafeAlert(long userid, AlertDTO alert) {
        User user = userService.getUserById(userid);

        if (!circleService.isUserInCircles(userid, alert.getCircles())) {
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
        Alert alertSave = new Alert(alert.getStatus(),alert.getDescription(),alertLocation, user, alert.getDuration(), alert.setActive(true), alert.setFirstNotification(true));
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
                FcmToken token = tokens.get(0);
                Location locationToSend;
                if (alertSave.getFirstNotification()){
                    locationToSend = alertLocation;
                }
                else {
                    locationToSend = user.getLocation();
                }

                sendNotificationUnSafe(
                        token.getFcmToken(),
                        "Unsafe Alert: " + user.getFirstName() + " " + user.getLastName(),
                        alert.getDescription(),
                        locationToSend.getLatitude(),
                        locationToSend.getLongitude(),
                        user.getUserId()
                );
                notifiedUserIds.add(userInCircle.getUserId());
            }
        }
        );

        if (alertSave.getFirstNotification()) {
            alertSave.setFirstNotification(false);
            alertRepository.save(alertSave);
        }

        return ResponseEntity.status(HttpStatus.OK).body("Unsafe Alert: " + alert.getStatus());
    }

    public ResponseEntity<String> stopAlert(long userId) {
        ActiveAlertDTO activeAlert = getActiveAlert(userId);
        if (activeAlert == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active alert found for the user");
        }

        Alert alert = alertRepository.findByAlertId(activeAlert.getAlertId()).getFirst();
        if (alert == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alert not found");
        }

        if (activeAlert.getCreatedAt() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Alert creation time is missing");
        }

        alert.setisActive(false);
        alert.setUpdatedAt(LocalDateTime.now());
        if (alert.getUpdatedAt() != null) {
            Duration duration = Duration.between(activeAlert.getCreatedAt(), alert.getUpdatedAt());
            String formattedDuration = formatDuration(duration);

            alert.setDurationOfAlert(formattedDuration);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to set alert update time");
        }

        alertRepository.save(alert);
        return ResponseEntity.status(HttpStatus.OK).body("Alert Stopped");
    }

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void stopAlertsAfter12Hours() {
        List<Alert> activeAlerts = alertRepository.findByIsActive(true);

        for (Alert alert : activeAlerts) {
            if (alert.getCreatedAt() != null && alert.getCreatedAt().isBefore(LocalDateTime.now().minusHours(12))) {
                try {
                    stopAlert(alert.getUser().getUserId());
                    System.out.println("Alert stopped after 12 hours for user ID: " + alert.getUser().getUserId());
                } catch (Exception e) {
                    System.err.println("Failed to stop alert for user ID: " + alert.getUser().getUserId());
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Checked for alerts older than 12 hours.");
    }

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void stopAlertsAfterDuration() {
        List<Alert> activeAlerts = alertRepository.findByIsActive(true);

        for (Alert alert : activeAlerts) {
            if (alert.getStatus() == SafetyStatus.UNSAFE) {
                String durationString = alert.getDurationOfAlert();
                if (durationString == null || durationString.isEmpty()) {
                    System.err.println("Skipping alert with null or empty duration: " + alert);
                    continue;
                }

                try {
                    Duration duration = Duration.parse(durationString);
                    if (alert.getCreatedAt().plus(duration).isBefore(LocalDateTime.now())) {
                        alert.setActive(false);
                        alertRepository.save(alert);
                        System.out.println("Alert stopped for user ID: " + alert.getUser().getUserId());
                    }
                } catch (Exception e) {
                    System.err.println("Error processing alert: " + alert);
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Checked for alerts older than 12 hours.");
    }

        public ResponseEntity<String> sendSOSAlert(long userId, AlertDTO alert) {
        User user = userService.getUserById(userId);
        List<User> users = userRepository.findAllByLocationIsNotNull();

        Location alertLocation = new Location(alert.getLocation().latitude(), alert.getLocation().longitude());
        locationRepository.save(alertLocation);
        Alert alertSave = new Alert(alert.getStatus(),alert.getDescription(),alertLocation, user, alert.getDuration(), alert.setActive(true), alert.setFirstNotification(true));
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

                Location locationToSend;
                if (alertSave.getFirstNotification()){
                    locationToSend = alertLocation;
                }
                else {
                    locationToSend = user.getLocation();
                }

                    sendNotificationSOS(
                            token.getFcmToken(),
                            "SOS Alert: " + alert.getStatus(),
                            alert.getDescription(),
                            locationToSend.getLatitude(),
                            locationToSend.getLongitude(),
                            user.getUserId()
                    );
                    notifiedUserIds.add(userInArea.getUserId());

            }
        }

            if (alertSave.getFirstNotification()) {
                alertSave.setFirstNotification(false);
                alertRepository.save(alertSave);
            }
        return ResponseEntity.status(HttpStatus.OK).body("SOS Alert: " + alert.getStatus());
    }

        public ActiveAlertDTO getActiveAlert(long userId) {
        User user = userService.getUserById(userId);
        List<Alert> alerts = alertRepository.findByUser(user);


        for (Alert alert : alerts) {
            if (alert.getisActive()) {


                if (alert.getStatus().equals(SafetyStatus.UNSAFE)) {
                    CircleAlert circleAlert = alert.getCircleAlerts().iterator().next();
                    if (circleService.isUserInCircle(circleAlert.getCircle().getCircleId(), userId)) {
                        return new ActiveAlertDTO(
                                new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                                alert.getUser().getUserId(),
                                alert.getUser().getFirstName(),
                                alert.getUser().getLastName(),
                                alert.getCreatedAt(),
                                alert.getStatus(),
                                alert.getAlertId()
                        );
                    } else {
                        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not in the circle of the alert");
                    }
                }

                if (alert.getStatus().equals(SafetyStatus.SOS)) {
                    return new ActiveAlertDTO(
                            new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                            alert.getUser().getUserId(),
                            alert.getUser().getFirstName(),
                            alert.getUser().getLastName(),
                            alert.getCreatedAt(),
                            alert.getStatus(),
                            alert.getAlertId()
                    );
                }
            }
        }
            return null;
    }

    public String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }

    public List<RequestAlertDTO> getLatestAlert() {
        return alertRepository.findByCreatedAtAfter(LocalDateTime.now().minusDays(1)).stream()
                .map(alert -> new RequestAlertDTO(
                    new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                    alert.getUser().getFirstName(),
                    alert.getUser().getLastName(),
                    alert.getStatus(),
                    alert.getDescription(),
                        alert.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<RequestAlertDTO> getLatestSOS() {
        return alertRepository.findByCreatedAtAfterAndStatus(LocalDateTime.now().minusDays(1), SafetyStatus.SOS).stream()
                .map(alert -> new RequestAlertDTO(
                        new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                        alert.getUser().getFirstName(),
                        alert.getUser().getLastName(),
                        alert.getStatus(),
                        alert.getDescription(),
                        alert.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<RequestAlertDTO> getAllAlertsByCircleIdAndUserId(long userId, long circleId) {
        User user = userService.getUserById(userId);
        Circle circle = circleService.getCircleById(circleId);
        List<CircleUser> circleUsers = circleUserRepository.findByUser(user);
        List<CircleAlert> circleAlerts = circleAlertRepository.findByCircle(circle);

        for (CircleUser circleUser : circleUsers) {
            if (circleService.isUserInCircle(circleId, circleUser.getUser().getUserId())) {
                List<RequestAlertDTO> circleAlertDTO = new ArrayList<>();
                for (CircleAlert circleAlert : circleAlerts) {
                    if(circleAlert.getAlert().getActive()) {
                        circleAlertDTO.add(new RequestAlertDTO(
                                circleAlert.getAlert().getUser().getUserId(),
                                new LocationDTO(circleAlert.getAlert().getLocation().getLatitude(), circleAlert.getAlert().getLocation().getLongitude()),
                                new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                                circleAlert.getAlert().getUser().getFirstName(),
                                circleAlert.getAlert().getUser().getLastName(),
                                circleAlert.getAlert().getStatus(),
                                circleAlert.getAlert().getDescription(),
                                circleAlert.getAlert().getCreatedAt()
                        ));
                    }
                }
                return circleAlertDTO;
            }
        }
        return new ArrayList<>();
    }

    public List<RequestAlertDTO> getAllAlertsByCircleId(long circleId) {
        Circle circle = circleService.getCircleById(circleId);
        List<CircleAlert> circleAlerts = circleAlertRepository.findByCircle(circle);

        List<RequestAlertDTO> circleAlertDTO = new ArrayList<>();
        for (CircleAlert circleAlert : circleAlerts) {
            if (circleAlert.getAlert().getActive()) {
                User user = circleAlert.getAlert().getUser();
                circleAlertDTO.add(new RequestAlertDTO(
                        new LocationDTO(circleAlert.getAlert().getLocation().getLatitude(), circleAlert.getAlert().getLocation().getLongitude()),
                        new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                        circleAlert.getAlert().getUser().getFirstName(),
                        circleAlert.getAlert().getUser().getLastName(),
                        circleAlert.getAlert().getStatus(),
                        circleAlert.getAlert().getDescription(),
                        circleAlert.getAlert().getCreatedAt()
                ));
            }
        }
        return circleAlertDTO;
    }


    public List<RequestAlertDTO> getAllAlertsByUserid(long userId) {
        User user = userService.getUserById(userId);
        List<Alert> alerts = alertRepository.findByUser(user);
        List<RequestAlertDTO> circleAlertDTO = new ArrayList<>();
        for (Alert alert : alerts) {
            if (alert.getStatus().equals(SafetyStatus.UNSAFE)) {
                Set<CircleAlert> circleAlerts = alert.getCircleAlerts();
                for (CircleAlert circleAlert : circleAlerts) {
                    if (circleService.isUserInCircle(circleAlert.getCircle().getCircleId(), alert.getUser().getUserId())) {
                        circleAlertDTO.add(new RequestAlertDTO(
                                new LocationDTO(circleAlert.getAlert().getLocation().getLatitude(), circleAlert.getAlert().getLocation().getLongitude()),
                                new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                                circleAlert.getAlert().getUser().getFirstName(),
                                circleAlert.getAlert().getUser().getLastName(),
                                circleAlert.getAlert().getStatus(),
                                circleAlert.getAlert().getDescription(),
                                circleAlert.getAlert().getCreatedAt()
                        ));
                    }
                }
            }

            if (alert.getStatus().equals(SafetyStatus.SOS)) {
                circleAlertDTO.add(new RequestAlertDTO(
                        new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                        new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                        alert.getUser().getFirstName(),
                        alert.getUser().getLastName(),
                        alert.getStatus(),
                        alert.getDescription(),
                        alert.getCreatedAt()
                ));
            }

        }
        return circleAlertDTO;
    }

    public List<RequestAlertDTO> getAllActiveAlertsByUserid(long userId) {
        User user = userService.getUserById(userId);
        List<Alert> alerts = alertRepository.findByUser(user);
        List<RequestAlertDTO> circleAlertDTO = new ArrayList<>();
        for (Alert alert : alerts) {
            if (alert.getStatus().equals(SafetyStatus.UNSAFE) && alert.getActive()) {
                Set<CircleAlert> circleAlerts = alert.getCircleAlerts();
                for (CircleAlert circleAlert : circleAlerts) {
                    if (circleService.isUserInCircle(circleAlert.getCircle().getCircleId(), alert.getUser().getUserId())) {
                        circleAlertDTO.add(new RequestAlertDTO(
                                new LocationDTO(circleAlert.getAlert().getLocation().getLatitude(), circleAlert.getAlert().getLocation().getLongitude()),
                                new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                                circleAlert.getAlert().getUser().getFirstName(),
                                circleAlert.getAlert().getUser().getLastName(),
                                circleAlert.getAlert().getStatus(),
                                circleAlert.getAlert().getDescription(),
                                circleAlert.getAlert().getCreatedAt()
                        ));
                    }
                }
            }

            if (alert.getStatus().equals(SafetyStatus.SOS) && alert.getActive()) {
                circleAlertDTO.add(new RequestAlertDTO(
                        new LocationDTO(alert.getLocation().getLatitude(), alert.getLocation().getLongitude()),
                        new LocationDTO(user.getLocation().getLatitude(), user.getLocation().getLongitude()),
                        alert.getUser().getFirstName(),
                        alert.getUser().getLastName(),
                        alert.getStatus(),
                        alert.getDescription(),
                        alert.getCreatedAt()
                ));
            }

        }
        return circleAlertDTO;
    }

}
