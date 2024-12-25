package org.safecircle.backend.dto;


import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.dto.LocationDTO;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

public class AlertDTO {

    private SafetyStatus status;
    private String description;
    private LocationDTO location;
    private long userId;
    private List<Long> circles;
    private Boolean isActive;
    private String duration;

    public AlertDTO() {
    }

    public AlertDTO(SafetyStatus status, String description, LocationDTO location, long userId) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.userId = userId;
    }

    public AlertDTO(SafetyStatus status, String description, LocationDTO location, long userId, List<Long> circles, Boolean isActive, Duration duration) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.userId = userId;
        this.circles = circles;
        this.isActive = isActive;
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

    public Boolean getActive() {
        return isActive;
    }

    public Boolean setActive(Boolean active) {
       return isActive = active;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}