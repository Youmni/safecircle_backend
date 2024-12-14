package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.safecircle.backend.dto.AlertDTO;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.FcmToken;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.FcmTokenRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.safecircle.backend.dto.FcmTokenDTO;

import java.math.BigDecimal;
import java.util.List;
@Service
public class AlertService {
    private final LocationService locationService;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private FcmTokenDTO fcmTokenDTO;
    private FcmToken fcmToken;

    @Autowired
    public AlertService(UserRepository userRepository, LocationService locationService, FcmTokenRepository fcmTokenRepository) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.fcmTokenRepository = fcmTokenRepository;
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
        List<User> users = userRepository.findAllByLocationIsNotNull();

        for (User user : users) {
            BigDecimal distance = locationService.calculateDistance(
                    alert.getLocation().latitude(),
                    alert.getLocation().longitude(),
                    user.getLocation().getLatitude(),
                    user.getLocation().getLongitude()
            );
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

        return ResponseEntity.ok().body("Notifications sent to users within 2km range.");
    }
}
