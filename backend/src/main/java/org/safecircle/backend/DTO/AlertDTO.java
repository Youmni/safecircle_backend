package org.safecircle.backend.dto;


import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.Location;

import java.math.BigDecimal;
import java.util.List;

public class AlertDTO {

    private SafetyStatus status;
    private String description;
    private LocationDTO location;
    private long userId;
    private List<Long> circles;

    public AlertDTO() {
    }

    public AlertDTO(SafetyStatus status, String description, LocationDTO location, long userId) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.userId = userId;
    }

    public AlertDTO(SafetyStatus status, String description, LocationDTO location, long userId, List<Long> circles) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.userId = userId;
        this.circles = circles;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public List<Long> getCircles() {
        return circles;
    }

    public void setCircles(List<Long> circles) {
        this.circles = circles;
    }
}