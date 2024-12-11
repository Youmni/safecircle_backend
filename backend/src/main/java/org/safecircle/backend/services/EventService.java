package org.safecircle.backend.services;

import org.safecircle.backend.models.Event;
import org.safecircle.backend.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private EventRepository eventRepository;
    private UserService userService;

    @Autowired
    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Event getEventById(int id) {
      return eventRepository.findByEventId(id).getFirst();
    }

    public Boolean checkValidEventById(int id) {
       Event event = eventRepository.findByEventId(id).getFirst();
        return event != null;
    }

    public void CreateEvent(Event event) {
        eventRepository.save(event);
    }

    public void UpdateEvent(Event event, int id) {
        checkValidEventById(id);
        eventRepository.save(event);
    }
    public void DeleteEventById(int id) {
        try {
            if (checkValidEventById(id)) {
                Event event = eventRepository.findByEventId(id).getFirst();
                eventRepository.delete(event);
            }
        }
        catch (Exception e) {}
    }
}
