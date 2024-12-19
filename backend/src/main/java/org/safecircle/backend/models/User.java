package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.safecircle.backend.enums.UserType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;

    @NotBlank
    @Size(min = 2, max = 50, message = "First name must be between 5 and 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Last name must be between 5 and 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password cannot be empty or null")
    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull(message = "Date of Birth is required")
    @Column(name = "date_of_birth")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "UserType must be specified and should be either ADMIN or USER")
    private UserType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Blacklist> blacklists;

    @OneToMany(mappedBy = "sender")
    private List<Invitation> sentInvitations;

    @OneToMany(mappedBy = "receiver")
    private List<Invitation> receivedInvitations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("user-alert")
    Set<UserAlert> userAlerts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("circle-user")
    Set<CircleUser> circleUsers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    @JsonManagedReference
    private Location location;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference ("fcmToken-user")
    private Set<FcmToken> fcmTokens;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    protected User() {}

    public User(String firstName, String lastName, String email, String password, String phoneNumber, LocalDate dateOfBirth, UserType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public @NotBlank @Size(min = 2, max = 50, message = "First name must be between 5 and 50 characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Size(min = 2, max = 50, message = "First name must be between 5 and 50 characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Size(min = 2, max = 50, message = "Last name must be between 5 and 50 characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(min = 2, max = 50, message = "Last name must be between 5 and 50 characters") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password cannot be empty or null") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be empty or null") String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull(message = "Date of Birth is required") LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull(message = "Date of Birth is required") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @NotNull(message = "UserType must be specified and should be either ADMIN or USER") UserType getType() {
        return type;
    }

    public void setType(@NotNull(message = "UserType must be specified and should be either ADMIN or USER") UserType type) {
        this.type = type;
    }

    public Set<Blacklist> getBlacklists() {
        return blacklists;
    }

    public void setBlacklists(Set<Blacklist> blacklists) {
        this.blacklists = blacklists;
    }


    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<UserAlert> getUserAlerts() {
        return userAlerts;
    }

    public void setUserAlerts(Set<UserAlert> userAlerts) {
        this.userAlerts = userAlerts;
    }

    public Set<CircleUser> getCircleUsers() {
        return circleUsers;
    }

    public void setCircleUsers(Set<CircleUser> circleUsers) {
        this.circleUsers = circleUsers;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Invitation> getSentInvitations() {
        return sentInvitations;
    }

    public void setSentInvitations(List<Invitation> sentInvitations) {
        this.sentInvitations = sentInvitations;
    }

    public List<Invitation> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(List<Invitation> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }

    public Set<FcmToken> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(Set<FcmToken> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }
}
