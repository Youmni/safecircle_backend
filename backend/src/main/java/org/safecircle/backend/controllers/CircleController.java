package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dto.CircleDTO;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.services.CircleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/circle")
public class CircleController {
    private final CircleService circleService;

    public CircleController(CircleService circleService) {
        this.circleService = circleService;
    }

    @CrossOrigin
    @GetMapping(value = "/{circleId}")
    public Circle getCircleById(@PathVariable long circleId) {
        return circleService.getCircleById(circleId);
    }

    @CrossOrigin
    @PostMapping(value = "/{userId}/create")
    public ResponseEntity<String> createCircle(@PathVariable long userId, @Valid @RequestBody CircleDTO circleDTO) {
        return circleService.createCircle(userId, circleDTO);
    }

    @CrossOrigin
    @PutMapping(value = "/{circleId}/update")
    public ResponseEntity<String> updateCircle(@Valid @RequestBody CircleDTO circleDTO, @PathVariable long circleId) {
        return circleService.updateCircle(circleId, circleDTO);
    }

    @CrossOrigin
    @DeleteMapping(value = "/{circleId}/remove")
    public ResponseEntity<String> deleteCircle(@PathVariable long circleId) {
        return circleService.deleteCircle(circleId);
    }

    @CrossOrigin
    @PutMapping(value = "{circleId}/remove/{userId}")
    public ResponseEntity<String> removeUserById(@PathVariable long circleId, @PathVariable long userId) {
        return circleService.removeUserById(circleId, userId);
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}/addUsers/{userIds}")
    public ResponseEntity<String> addUserByIds(@PathVariable long circleId, @PathVariable List<Long> userIds) {
        return circleService.addUsersToCircle(circleId, userIds);
    }
}
