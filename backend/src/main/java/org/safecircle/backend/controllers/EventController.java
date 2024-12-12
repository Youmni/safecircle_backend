package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.DTO.EventDTO;
import org.safecircle.backend.models.User;
import org.safecircle.backend.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.safecircle.backend.dto.UserDTO;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private final EventService eventService;
    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<String> AddEvent(@Valid @RequestBody EventDTO event) {
        return eventService.createEvent(event);
    }
}
