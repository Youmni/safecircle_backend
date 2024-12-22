package org.safecircle.backend.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.safecircle.backend.dto.CircleDTO;
import org.safecircle.backend.dto.UserDTO;
import org.safecircle.backend.dto.UserRequestDTO;
import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.models.*;
import org.safecircle.backend.repositories.CircleAlertRepository;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.repositories.CircleUserRepository;
import org.safecircle.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            return null;
        }
        return userRepository.findByUserId(userId).getFirst();
    }

    public Circle getCircleById(long circleId) {
        return circleRepository.findById(circleId).orElse(null);
    }

    public List<Circle> getCirclesByUserId(long userId) {
        if(!userService.isUserValid(userId)) {
            return null;
        }
        User user = getUserById(userId);
        List<CircleUser> listCirclesUsers = circleUserRepository.findByUser(user);
        return listCirclesUsers.stream().map(CircleUser::getCircle).toList();
    }

    public ResponseEntity<List<UserRequestDTO>> getUsersByCircleId(long circleId) {
        if(!circleRepository.existsByCircleId(circleId)) {
            return null;
        }
        List<CircleUser> circleUserList = circleUserRepository.findByCircle(circleRepository.findByCircleId(circleId).getFirst());
        List<UserRequestDTO> userDTOList = new ArrayList<>();
        for (CircleUser circleUser : circleUserList) {
            User user = circleUser.getUser();
            UserRequestDTO userDTO = new UserRequestDTO(
                    user.getFirstName(),
                    user.getEmail(),
                    user.getLastName(),
                    null,
                    user.getPhoneNumber(),
                    user.getDateOfBirth()
            );
            userDTOList.add(userDTO);
        }
        return ResponseEntity.ok(userDTOList);
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
            if(!isCircleValid(circleId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Circle not found");
            }
            if(!userService.isUserValid(userId)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
            }
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
            if(!userService.isUserValid(userId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            List<Circle> listCircles = getCirclesByUserId(userId);
            int numberOfRegularCircles = 0;
            for(Circle circle : listCircles) {
                if (circle.getCircleType() == CircleType.REGULAR){
                    numberOfRegularCircles++;
                }
            }
            if (numberOfRegularCircles >= 5) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The User already has 5 Circles");
            }
            Circle circle = new Circle(circleDTO.getCircleType(), circleDTO.isAvailable(), circleDTO.getCircleName());

            System.out.println(circleDTO.getCircleName());
            circleRepository.save(circle);
            addUserToCircleCheck(circle.getCircleId(), userId);

            return ResponseEntity.ok("Successfully created circle");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("There was an error creating the Circle"+ e.getMessage());
        }
    }

    public ResponseEntity<String> updateCircle(long circleId, CircleDTO circleDTO) {
        try{
            if(!circleRepository.existsByCircleId(circleId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Circle not found");
            }
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

    public boolean isUserInCircles(long userId, List<Long> circles){
        for(long circle : circles){
            if(!isUserInCircle(circle, userId)) {
                return false;
            }
        }
        return true;
    }
}