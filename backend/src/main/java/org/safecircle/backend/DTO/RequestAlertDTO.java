package org.safecircle.backend.dto;

import org.safecircle.backend.enums.SafetyStatus;

public class RequestAlertDTO {

    private LocationDTO location;
    private String firstName;
    private String lastName;
    private SafetyStatus status;
    private String description;

    public RequestAlertDTO(LocationDTO location, String firstName, String lastName, SafetyStatus status, String description) {
        this.location = location;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.description = description;
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
}
