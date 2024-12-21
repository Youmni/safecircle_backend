package org.safecircle.backend.dto;

import org.safecircle.backend.enums.UserType;
import org.safecircle.backend.models.Blacklist;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.CircleUser;
import org.safecircle.backend.models.CircleUserKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UserDTO {

    private long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private UserType userType;
    private Set<Long> blacklists;
    private Set<CircleUserKey> circles;

    public UserDTO(long userId, String firstName, String lastName, String email,LocalDate dateOfBirth, String phoneNumber, UserType userType, Set<Long> blacklists, Set<CircleUserKey> circles) {
        this.userId = userId;
        this.circles = circles;
        this.blacklists = blacklists;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Set<Long> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(Set<Long> blacklists) {
        this.blacklists = blacklists;
    }

    public Set<CircleUserKey> getCircles() {
        return circles;
    }

    public void setCircles(Set<CircleUserKey> circles) {
        this.circles = circles;
    }
}
