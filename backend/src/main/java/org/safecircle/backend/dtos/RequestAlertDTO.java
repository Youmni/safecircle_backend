package org.safecircle.backend.dtos;

import org.safecircle.backend.enums.SafetyStatus;

import java.time.LocalDateTime;

public class RequestAlertDTO {

    private LocationDTO location;
    private LocationDTO alertLocation;
    private String firstName;
    private String lastName;
    private SafetyStatus status;
    private String description;
    private LocalDateTime createdAt;

    public RequestAlertDTO(LocationDTO location, String firstName, String lastName, SafetyStatus status, String description, LocalDateTime createdAt) {
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public RequestAlertDTO(LocationDTO location, LocationDTO alertLocation, String firstName, String lastName, SafetyStatus status, String description, LocalDateTime createdAt) {
        this.location = location;
        this.alertLocation = alertLocation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
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

    public SafetyStatus getStatus() {
        return status;
    }

    public void setStatus(SafetyStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocationDTO getAlertLocation() {
        return alertLocation;
    }

    public void setAlertLocation(LocationDTO alertLocation) {
        this.alertLocation = alertLocation;
    }
}
