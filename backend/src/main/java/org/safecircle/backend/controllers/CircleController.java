package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dtos.CircleDTO;
import org.safecircle.backend.dtos.CircleRequestDTO;
import org.safecircle.backend.dtos.UserRequestDTO;
import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.services.CircleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/circle")
public class CircleController {
    private final CircleService circleService;
    private final CircleRepository circleRepository;

    public CircleController(CircleService circleService, CircleRepository circleRepository) {
        this.circleService = circleService;
        this.circleRepository = circleRepository;
    }


    @CrossOrigin
    @GetMapping(value = "/all")
    public List<CircleRequestDTO> getAllCircles() {
        return circleRepository.findAll().stream()
                .map(circle -> new CircleRequestDTO(
                        circle.getCircleId(),
                        circle.getCircleName(),
                        circle.getCircleType(),
                        circle.isAvailable(),
                        circle.getCreatedAt(),
                        circle.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }



    @CrossOrigin
    @GetMapping(value = "/{circleId}")
    public ResponseEntity<CircleDTO> getCircleById(@PathVariable long circleId) {
        Circle circle = circleService.getCircleById(circleId);
        if(circle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        else{
            CircleDTO circleDTO = new CircleDTO(
                    circle.getCircleName(),
                    circle.getCircleType(),
                    circle.isAvailable(),
                    circle.getCircleId()
            );
            return ResponseEntity.ok(circleDTO);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/getAll/{userId}")
    public ResponseEntity<List<CircleDTO>> getCirclesByUserId(@PathVariable long userId) {
        List<Circle> listCircles = circleService.getCirclesByUserId(userId);
        if(listCircles == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        else {
            List<CircleDTO> circleDTOList = new ArrayList<>();
            for(Circle circle : listCircles) {
                CircleDTO circleDTO = new CircleDTO(
                        circle.getCircleName(),
                        circle.getCircleType(),
                        circle.isAvailable(),
                        circle.getCircleId()
                );
                circleDTOList.add(circleDTO);
            }
            return ResponseEntity.ok(circleDTOList);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{circleId}/showAllUsers")
    public ResponseEntity<List<UserRequestDTO>> getAllUsersByCircleId(@PathVariable long circleId) {
        return circleService.getUsersByCircleId(circleId);
    }

    @CrossOrigin
    @GetMapping(value = "/event/all")
    public List<CircleRequestDTO> getAllEventCircles() {
        return circleService.getCircleByType(CircleType.EVENT);
    }

    @CrossOrigin
    @PostMapping(value = "/{userId}/create")
    public ResponseEntity<String> createCircle(@PathVariable long userId, @Valid @RequestBody CircleDTO circleDTO) {
        return circleService.createCircle(userId, circleDTO);
    }

    @CrossOrigin
    @PutMapping(value = "/{circleId}/update")
    public ResponseEntity<String> updateCircle(@PathVariable long circleId, @RequestParam(required = false) String circleName) {
        return circleService.updateCircle(circleId, circleName);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{circleId}/delete")
    public ResponseEntity<String> deleteCircle(@PathVariable long circleId) {
        return circleService.deleteCircle(circleId);
    }

    @CrossOrigin
    @PutMapping(value = "/{circleId}/remove/{userId}")
    public ResponseEntity<String> removeUserById(@PathVariable long circleId, @PathVariable long userId) {
        return circleService.removeUserById(circleId, userId);
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}/add/{userIds}")
    public ResponseEntity<String> addUserByIds(@PathVariable long circleId, @PathVariable List<Long> userIds) {
        return circleService.addUsersToCircle(circleId, userIds);
    }

    @CrossOrigin
    @GetMapping("/users/{circleId}")
    public ResponseEntity<List<UserRequestDTO>> getUsersByCircleId(@PathVariable long circleId) {
        return circleService.getUsersByCircleId(circleId);
    }
}
