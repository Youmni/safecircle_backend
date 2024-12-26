package org.safecircle.backend.dtos;

import org.safecircle.backend.enums.SafetyStatus;

import java.time.LocalDateTime;

public class ActiveAlertDTO {
    private long AlertId;
    private LocationDTO location;
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private SafetyStatus eventStatus;

    public ActiveAlertDTO(LocationDTO location, Long userId, String firstName, String lastName, LocalDateTime createdAt, SafetyStatus eventStatus, long alertId) {
        this.AlertId = alertId;
        this.location = location;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.eventStatus = eventStatus;
    }

    public long getAlertId() {
        return AlertId;
    }

    public void setAlertId(long alertId) {
        AlertId = alertId;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public SafetyStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(SafetyStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
