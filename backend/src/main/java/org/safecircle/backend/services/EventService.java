package org.safecircle.backend.services;

import org.safecircle.backend.dto.EventDTO;
import org.safecircle.backend.dto.LocationDTO;
import org.safecircle.backend.enums.EventStatus;
import org.safecircle.backend.models.Event;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.safecircle.backend.repositories.EventRepository;
import org.safecircle.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.safecircle.backend.dto.UserDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class EventService {
    private EventRepository eventRepository;
    private UserService userService;
    private LocationRepository locationRepository;

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Event getEventById(long id) {
      return eventRepository.findByEventId(id).getFirst();
    }

    public Boolean isEventValid(Long id) {
       Event event = eventRepository.findByEventId(id).getFirst();
        return event != null;
    }

    public ResponseEntity<String> createEvent(EventDTO eventDTO) {
        try {

            Location location = new Location(eventDTO.getLocation().latitude(), eventDTO.getLocation().longitude());
            locationRepository.save(location);

            Event event = new Event(
                    eventDTO.getUserCountEstimate(),
                    eventDTO.getEventName(),
                    eventDTO.getEmail(),
                    eventDTO.getEventStatus(),
                    eventDTO.getStartDate(),
                    eventDTO.getEndDate(),
                    location);

            eventRepository.save(event);
            return ResponseEntity.status(HttpStatus.CREATED).body("Event created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create event" + e.getMessage());
        }
    }


    public ResponseEntity<String> UpdateEventById(long eventId, EventDTO eventDTO) {
        try {
            if(!isEventValid(eventId)) {
                return ResponseEntity.badRequest().body("Event not found");
            } else {
               Event event = getEventById(eventId);

               event.setEventName(eventDTO.getEventName());
               event.setUserCountEstimate(eventDTO.getUserCountEstimate());
               event.setEventStatus(event.getEventStatus());
               event.setStartDate(eventDTO.getStartDate());
               event.setEndDate(eventDTO.getEndDate());
               eventRepository.save(event);
               return ResponseEntity.status(HttpStatus.OK).body("Event updated");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update event");
        }
    }
    public void DeleteEventById(long id) {
        try {
            if (isEventValid(id)) {
                Event event = eventRepository.findByEventId(id).getFirst();
                eventRepository.delete(event);
            }
        }
        catch (Exception e) {}
    }

    public List<EventDTO> getAllEvents(){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findAll().forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude()),
                    event.getEndDate(),
                    event.getStartDate()
            );
            events.add(eventDTO);
        });
        return events;
    }

    public List<EventDTO> getEventsByName(String name){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventNameContaining(name).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude()),
                    event.getEndDate(),
                    event.getStartDate()
            );
            events.add(eventDTO);
        });
        return events;
    }

    public List<EventDTO> getEventsById(long id){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventId(id).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude()),
                    event.getEndDate(),
                    event.getStartDate()
            );
            events.add(eventDTO);
        });
        return events;
    }

    public List<EventDTO> getEventsByStatus(EventStatus status){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventStatus(status).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude()),
                    event.getEndDate(),
                    event.getStartDate()
            );
            events.add(eventDTO);
        });
        return events;
    }



}
