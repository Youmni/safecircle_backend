package org.safecircle.backend.services;

import jakarta.persistence.EntityNotFoundException;
import org.safecircle.backend.dto.CircleDTO;
import org.safecircle.backend.models.*;
import org.safecircle.backend.repositories.CircleAlertRepository;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.repositories.CircleUserRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CircleService {
    private final UserService userService;
    private final CircleUserService circleUserService;
    private CircleRepository circleRepository;
    private UserRepository userRepository;
    private CircleUserRepository circleUserRepository;
    private CircleAlertRepository circleAlertRepository;

    @Autowired
    public CircleService(CircleRepository circleRepository, UserRepository userRepository, CircleUserRepository circleUserRepository, CircleAlertRepository circleAlertRepository, UserService userService, CircleUserService circleUserService) {
        this.circleRepository = circleRepository;
        this.userRepository = userRepository;
        this.circleUserRepository = circleUserRepository;
        this.circleAlertRepository = circleAlertRepository;
        this.userService = userService;
        this.circleUserService = circleUserService;
    }

    public User getUserById(long userId) {
        if(!userService.isUserValid(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return userRepository.findByUserId(userId).getFirst();
    }

    public Circle getCircleById(long circleId) {
        if(!isCircleValid(circleId)) {
            throw new EntityNotFoundException("Circle not found");
        }
        return circleRepository.findByCircleId(circleId).getFirst();
    }

    public ResponseEntity<String> addUserById(long circleId, long userId) {
        try{
            Circle circle = getCircleById(circleId);
            User user = getUserById(userId);
            CircleUserKey circleUserkey = new CircleUserKey(circleId, userId);
            CircleUser circleUser = new CircleUser(circle, user);
            circleUser = circleUserRepository.save(circleUser);
            circleUser.setId(circleUserkey);
            circleUserRepository.save(circleUser);
            return ResponseEntity.ok("Successfully added user to circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error creating the Circle");
        }
    }

    public ResponseEntity<String> addUsersToCircle(long circleId, List<Long> userIds) {
        if(!isCircleValid(circleId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Circle not found");
        }
        if(!userService.isValidUserIds(userIds)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is an invalid user ID");
        }

        for(Long userId : userIds) {
            if (!isUserInCircle(circleId, userId)) {
                addUserToCircleCheck(circleId, userId);
            }else{
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("The user(s) were already in the circle");
            }
        }
        return ResponseEntity.ok("Successfully added users to the circle");
    }

    public void addUserToCircleCheck(long circleId, long userId) {
        if(!isCircleValid(circleId)){
            return;
        }
        if(!userService.isUserValid(userId)){
            return;
        }

        Circle circle = getCircleById(circleId);
        User user = userService.getUserById(userId);

        CircleUserKey key = new CircleUserKey(circleId, userId);
        CircleUser circleUser = new CircleUser(circle, user);
        circleUser.setId(key);
        circleUserRepository.save(circleUser);
        circleUserRepository.flush();
    }

    public ResponseEntity<String> removeUserById(long circleId, long userId) {
        try{
            Circle circle = getCircleById(circleId);
            User user = getUserById(userId);
            List<CircleUser> circleUserList = circleUserRepository.findByUserAndCircle(user, circle);
            if (circleUserList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found in the circle");
            }
            circleUserRepository.delete(circleUserList.getFirst());
            return ResponseEntity.ok("Successfully removed user from circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error creating the Circle");
        }
    }

    public ResponseEntity<String> createCircle(long userId, CircleDTO circleDTO) {
        try {
            Circle circle = new Circle(circleDTO.getCircleType(), circleDTO.isAvailable(), circleDTO.getCircleName());
             circle = circleRepository.save(circle);
            addUserToCircleCheck(circle.getCircleId(), userId);

            return ResponseEntity.ok("Successfully created circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error creating the Circle");
        }
    }

    public ResponseEntity<String> updateCircle(long circleId, CircleDTO circleDTO) {
        try{
            Circle circle = getCircleById(circleId);
            circle.setCircleType(circleDTO.getCircleType());
            circle.setAvailable(circleDTO.isAvailable());
            circle.setCircleName(circleDTO.getCircleName());
            circleRepository.save(circle);

            return ResponseEntity.ok("Successfully updated circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error updating the Circle");
        }
    }

    public ResponseEntity<String> deleteCircle(long circleId) {
        try{
            if (!circleRepository.existsByCircleId(circleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Circle with ID " + circleId + " not found.");
            }
            Circle circle = getCircleById(circleId);

            List<CircleUser> circleUserList = circleUserRepository.findByCircle(circle);
            if(!circleUserList.isEmpty()){
                circleUserRepository.deleteAll(circleUserList);
            }
            List<CircleAlert> circleAlertList = circleAlertRepository.findByCircle(circle);
            if(!circleAlertList.isEmpty()){
                circleAlertRepository.deleteAll(circleAlertList);
            }
            circleRepository.deleteById(circleId);
            return ResponseEntity.ok("Successfully deleted the Circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error deleting the Circle");
        }
    }

    public boolean isCircleValid(long circleId) {
        return circleRepository.existsByCircleId(circleId);
    }

    public boolean isUserInCircle(long circleId, long userId) {
        Circle circle = getCircleById(circleId);
        User user = getUserById(userId);
        List<CircleUser> circleUserList = circleUserRepository.findByUserAndCircle(user, circle);
        return !circleUserList.isEmpty();
    }

}
