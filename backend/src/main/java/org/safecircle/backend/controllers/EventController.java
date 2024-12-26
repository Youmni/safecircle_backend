package org.safecircle.backend.controllers;

import jakarta.validation.Valid;
import org.safecircle.backend.dtos.EventDTO;
import org.safecircle.backend.enums.EventStatus;
import org.safecircle.backend.models.Event;
import org.safecircle.backend.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/{id}/create")
    public ResponseEntity<String> AddEvent(@PathVariable long id, @Valid @RequestBody EventDTO event) {
        return eventService.createEvent(id, event);
    }

    @CrossOrigin
    @PostMapping("/{id}/update")
    public ResponseEntity<String> UpdateEvent(@PathVariable long id, @Valid @RequestBody EventDTO event) {
        return eventService.UpdateEventById(id, event);
    }

    @CrossOrigin
    @PostMapping("/{eventid}/add/{userId}")
    public ResponseEntity<String> AddUsersToEvent( @PathVariable long eventid, @PathVariable List<Long> userId) {
        return eventService.AddUsersToEvent(eventid, userId);
    }

    @CrossOrigin
    @PostMapping("/{id}/request")
    public ResponseEntity<String> AddEventRequest(@PathVariable long id, @Valid @RequestBody EventDTO event) {
        event.setEventStatus(EventStatus.PENDING);
        return eventService.createEvent(id, event);
    }

    @CrossOrigin
    @GetMapping("/approved")
    public List<Event> getApprovedEvents() {
        return eventService.getAllApprovedEvents();
    }

    @CrossOrigin
    @GetMapping("/all")
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
