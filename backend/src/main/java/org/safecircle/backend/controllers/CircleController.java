package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dto.CircleDTO;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.services.CircleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/circle")
public class CircleController {
    private final CircleService circleService;

    public CircleController(CircleService circleService) {
        this.circleService = circleService;
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}")
    public Circle getCircleById(@PathVariable long circleId) {
        return circleService.getCircleById(circleId);
    }

    @CrossOrigin
    @PostMapping(value = "/create")
    public ResponseEntity<String> createCircle(@Valid @RequestBody CircleDTO circleDTO) {
        return circleService.createCircle(circleDTO);
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}/update")
    public ResponseEntity<String> updateCircle(@Valid @RequestBody CircleDTO circleDTO, @PathVariable long circleId) {
        return circleService.updateCircle(circleId, circleDTO);
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}/remove")
    public ResponseEntity<String> deleteCircle(@PathVariable long circleId) {
        return circleService.deleteCircle(circleId);
    }

    @CrossOrigin
    @PostMapping(value = "/{circleId}/add/{userId}")
    public ResponseEntity<String> addUserById(@PathVariable long circleId, @PathVariable long userId) {
        return circleService.addUserById(circleId, userId);
    }

    @CrossOrigin
    @PostMapping(value = "{circleId}/remove/{userId}")
    public ResponseEntity<String> removeUserById(@PathVariable long circleId, @PathVariable long userId) {
        return circleService.removeUserById(circleId, userId);
    }
}
