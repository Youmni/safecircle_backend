package org.safecircle.backend.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AlertService {
    private LocationService locationService;
    private UserRepository userRepository;

    @Autowired
    public UserRepository getUserRepository(UserRepository userRepository) {
        return this.userRepository;
    }
    @Autowired
    public LocationService getLocationService(LocationService locationService) {
        return locationService;
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
    public ResponseEntity<String> sendAlert(Alert alert) {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            BigDecimal distance = locationService.calculateDistance(
                    alert.getLocation().getLatitude(),
                    alert.getLocation().getLongitude(),
                    user.getLocation().getLatitude(),
                    user.getLocation().getLongitude()
            );
            BigDecimal maxDistance = new BigDecimal("2.0");
            if (distance.compareTo(maxDistance) <= 0 && user.getFcmToken() != null) {
                sendNotification(
                        "Emergency Alert: " + alert.getStatus(),
                        alert.getDescription(),
                        alert.getLocation().getLatitude(),
                        alert.getLocation().getLongitude(),
                        user.getFcmToken()
                );
            }
        }

        return ResponseEntity.ok().body("Notifications sent to users within 2km range.");
    }
}
