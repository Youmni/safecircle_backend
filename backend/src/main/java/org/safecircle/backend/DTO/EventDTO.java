package org.safecircle.backend.dto;

import org.safecircle.backend.enums.EventStatus;

import java.time.LocalDate;

public class EventDTO {

    private int userCountEstimate;
    private String eventName;
    private EventStatus eventStatus;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocationDTO location;

    public EventDTO(int userCountEstimate, String eventName, EventStatus eventStatus, String email, LocationDTO location, LocalDate endDate, LocalDate startDate) {
        this.userCountEstimate = userCountEstimate;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.email = email;
        this.location = location;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public int getUserCountEstimate() {
        return userCountEstimate;
    }

    public void setUserCountEstimate(int userCountEstimate) {
        this.userCountEstimate = userCountEstimate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
