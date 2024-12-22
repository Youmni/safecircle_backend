package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dto.EventDTO;
import org.safecircle.backend.enums.EventStatus;
import org.safecircle.backend.models.User;
import org.safecircle.backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.safecircle.backend.dto.UserDTO;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(final EventService eventService) {
        this.eventService = eventService;
    }

    @CrossOrigin
    @PostMapping("/add")
    public ResponseEntity<String> AddEvent(@Valid @RequestBody EventDTO event) {
        return eventService.createEvent(event);
    }

    @CrossOrigin
    @PostMapping("/request")
    public ResponseEntity<String> AddEventRequest(@Valid @RequestBody EventDTO event) {
        event.setEventStatus(EventStatus.PENDING);
        return eventService.createEvent(event);
    }

    @CrossOrigin
    @GetMapping()
    public List<EventDTO> GetAllEvents() {
        return eventService.getAllEvents();
    }

    @CrossOrigin
    @GetMapping("/name")
    public List<EventDTO> GetEventByName(@Valid @RequestParam(value = "name") String name) {
        return eventService.getEventsByName(name);
    }

    @CrossOrigin
    @GetMapping("/{eventId}")
    public List<EventDTO> GetEventsById(@Valid @PathVariable long eventId) {
        return eventService.getEventsById(eventId);
    }

    @CrossOrigin
    @GetMapping("/status")
    public List<EventDTO> GetEventsByStatus(@Valid @RequestParam(value = "status") EventStatus status) {
        return eventService.getEventsByStatus(status);
    }
}
