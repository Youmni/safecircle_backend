package org.safecircle.backend.DTO;

import java.time.LocalDate;

public class EventDTO {

    private int userCountEstimate;
    private String eventName;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;

    public EventDTO(int userCountEstimate, String eventName, String email, LocalDate startDate, LocalDate endDate) {
        this.userCountEstimate = userCountEstimate;
        this.eventName = eventName;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
