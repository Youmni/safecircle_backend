package org.safecircle.backend.services;

import org.safecircle.backend.dtos.CircleDTO;
import org.safecircle.backend.dtos.EventDTO;
import org.safecircle.backend.dtos.LocationDTO;
import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.enums.EventStatus;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.Event;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.repositories.CircleRepository;
import org.safecircle.backend.repositories.EventRepository;
import org.safecircle.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class EventService {
    private EventRepository eventRepository;
    private UserService userService;
    private CircleService circleService;
    private CircleRepository circleRepository;
    private CircleDTO circleDTO;

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
    public void setCircleService(CircleService circleService) {this.circleService = circleService;}

    @Autowired
    public void setCircleRepository (CircleRepository circleRepository) {this.circleRepository = circleRepository;}

    @Autowired
    public void setLocationRepository(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

   /* @CrossOrigin
    @RequestMapping(method = RequestMethod.GET)
    public List<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        eventRepository.findAll().forEach(events::add);
        return events;
    }*/

    public Event getEventById(long id) {
      return eventRepository.findByEventId(id).getFirst();
    }

    public Boolean isEventValid(Long id) {
       Event event = eventRepository.findByEventId(id).getFirst();
        return event != null;
    }

    public ResponseEntity<String> createEvent(Long id , EventDTO eventDTO) {
        try {

            Location location = new Location(eventDTO.getLocation().latitude(), eventDTO.getLocation().longitude());
            locationRepository.save(location);

            if (!userService.isUserValid(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The owner is not valid");
            }
            Circle eventcircle = new Circle(CircleType.EVENT, true, eventDTO.getEventName());
            circleRepository.save(eventcircle);

            Event event = new Event(
                    eventDTO.getUserCountEstimate(),
                    eventDTO.getEventName(),
                    eventDTO.getEmail(),
                    eventDTO.getEventStatus(),
                    eventDTO.getStartDate(),
                    eventDTO.getEndDate(),
                    location,
                    eventcircle
                    );

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

    public ResponseEntity<String> AddUsersToEvent(long eventId, List<Long> users) {
        try {
            if (!isEventValid(eventId)) {
                return ResponseEntity.badRequest().body("Event not found");
            } else {
                Event event = getEventById(eventId);
                for (Long userId : users) {
                    if (!userService.isUserValid(userId)) {
                        return ResponseEntity.badRequest().body("User not found");
                    }
                }
                circleService.addUsersToCircle(event.getCircle().getCircleId(), users);
                eventRepository.save(event);
                return ResponseEntity.status(HttpStatus.OK).body("Users added to event");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add users to event");
        }
    }

    public List<EventDTO> getAllEvents(){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findAll().forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getEventId(),
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    event.getStartDate(),
                    event.getEndDate(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude()));
            events.add(eventDTO);
        });
        return events;
    }

    public List<Event> getAllApprovedEvents(){
        List<Event> events = new ArrayList<>();
        eventRepository.findByEventStatus(EventStatus.APPROVED).forEach(event -> {
            if (event.getStartDate().plusDays(2).isAfter(LocalDate.now())) {
                events.add(event);
            }
        });
        return events;
    }

    public List<EventDTO> getEventsByName(String name){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventNameContaining(name).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getEventId(),
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    event.getStartDate(),
                    event.getEndDate(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude())
            );
            events.add(eventDTO);
        });
        return events;
    }

    public List<EventDTO> getEventsById(long id){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventId(id).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getEventId(),
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    event.getStartDate(),
                    event.getEndDate(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude())
            );
            events.add(eventDTO);
        });
        return events;
    }

    public List<EventDTO> getEventsByStatus(EventStatus status){
        List<EventDTO> events = new ArrayList<>();
        eventRepository.findByEventStatus(status).forEach(event -> {
            EventDTO eventDTO = new EventDTO(
                    event.getEventId(),
                    event.getUserCountEstimate(),
                    event.getEventName(),
                    event.getEventStatus(),
                    event.getEmail(),
                    event.getStartDate(),
                    event.getEndDate(),
                    new LocationDTO(event.getLocation().getLatitude(), event.getLocation().getLongitude())
            );
            events.add(eventDTO);
        });
        return events;
    }



}
