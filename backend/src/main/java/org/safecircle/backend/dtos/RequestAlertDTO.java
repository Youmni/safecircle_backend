package org.safecircle.backend.dtos;

import org.safecircle.backend.enums.SafetyStatus;

import java.time.LocalDateTime;

public class RequestAlertDTO {

    private long userId;
    private LocationDTO location;
    private LocationDTO userLocation;
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

    public RequestAlertDTO(LocationDTO location, LocationDTO userLocation, String lastName, String firstName, SafetyStatus status, String description, LocalDateTime createdAt) {
        this.location = location;
        this.userLocation = userLocation;
        this.lastName = lastName;
        this.firstName = firstName;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
    }

    public RequestAlertDTO(long userId, LocationDTO location, LocationDTO userLocation, String firstName, String lastName, SafetyStatus status, String description, LocalDateTime createdAt) {
        this.userId = userId;
        this.location = location;
        this.userLocation = userLocation;
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

    public LocationDTO getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LocationDTO userLocation) {
        this.userLocation = userLocation;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
