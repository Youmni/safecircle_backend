package org.safecircle.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.CircleUser;
import org.safecircle.backend.models.CircleUserKey;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.repositories.CircleUserRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CircleService {
    private CircleRepository circleRepository;
    private UserRepository userRepository;
    private CircleUserRepository circleUserRepository;

    @Autowired
    public CircleService(CircleRepository circleRepository, UserRepository userRepository, CircleUserRepository circleUserRepository) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
        this.circleUserRepository = circleUserRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Circle getCircleById(Long circleId) {
        return circleRepository.findById(circleId).orElse(null);
    }

    public boolean isCircleAvailable(Long circleId) {
        Circle circle = getCircleById(circleId);
        return circle.isAvailable();
    }

    public boolean addUserById(Long circleId, Long userId) {
        Circle circle = getCircleById(circleId);
        User user = getUserById(userId);

        if(circleUserRepository.findByUserAndCircle(user, circle).isEmpty()){
            return false;
        }
        else{
            CircleUserKey circleUserkey = new CircleUserKey(circleId, userId);
            CircleUser circleUser = new CircleUser(circle, user);
            circleUser = circleUserRepository.save(circleUser);
            circleUser.setId(circleUserkey);
            circleUserRepository.save(circleUser);
            return true;
        }
    }
}
