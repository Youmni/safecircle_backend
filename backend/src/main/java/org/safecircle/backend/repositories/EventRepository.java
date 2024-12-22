package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.EventStatus;
import org.safecircle.backend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventId(long eventId);
    List<Event> findByUserCountEstimateAfter(long userCountEstimateAfter);
    List<Event> findByUserCountEstimateBefore(long userCountEstimateBefore);
    List<Event> findByUserCountEstimateBetween(long from, long to);
    List<Event> findByEmail(String email);
    List<Event> findByEventName(String eventName);
    List<Event> findByEventNameContaining(String eventName);
    List<Event> findByEmailContaining(String email);
    List<Event> findByEventStatus(EventStatus status);



    List<Event> findByStartDateBefore(LocalDate startDate);
    List<Event> findByStartDateAfter(LocalDate startDate);
    List<Event> findByStartDateBetween(LocalDate from, LocalDate to);

    List<Event> findByEndDateBefore(LocalDate endDate);
    List<Event> findByEndDateAfter(LocalDate endDate);
    List<Event> findByEndDateBetween(LocalDate from, LocalDate to);
}
