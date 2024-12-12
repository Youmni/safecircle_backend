package org.safecircle.backend.dto;


import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.Location;

public class AlertDTO {

    private SafetyStatus status;
    private String description;
    private Location location;
    private long userId;

    public AlertDTO(SafetyStatus status, String description, Location location, long userId) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.userId = userId;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}