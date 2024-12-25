package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.safecircle.backend.enums.SafetyStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private long alertId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.")
    @Column(name = "alert")
    private SafetyStatus status;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("circle-alert")
    Set<CircleAlert> circleAlerts;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("user-alert")
    Set<UserAlert> userAlerts;

    @OneToOne
    @JoinColumn(name = "location_id", nullable = false)
    @JsonBackReference("alert-location")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

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


    @Column(name = "Duration")
    private String durationOfAlert;

    @NotNull(message = "Active must be set to true or false.")
    @Column(name = "Active")
    private Boolean isActive;

    protected Alert() {}

    public Alert(SafetyStatus status, String description, Location location, User user, String durationOfAlert, Boolean Isactive) {
        this.status = status;
        this.description = description;
        this.location = location;
        this.user = user;
        this.durationOfAlert = durationOfAlert;
        this.isActive = Isactive;
    }

    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }

    public @NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.") SafetyStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.") SafetyStatus status) {
        this.status = status;
    }

    public @NotBlank(message = "Description is required") @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description is required") @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters") String description) {
        this.description = description;
    }

    public Set<CircleAlert> getCircleAlerts() {
        return circleAlerts;
    }

    public void setCircleAlerts(Set<CircleAlert> circleAlerts) {
        this.circleAlerts = circleAlerts;
    }

    public Set<UserAlert> getUserAlerts() {
        return userAlerts;
    }

    public void setUserAlerts(Set<UserAlert> userAlerts) {
        this.userAlerts = userAlerts;
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

    public String getDurationOfAlert() {
        return durationOfAlert;
    }

    public void setDurationOfAlert(String durationOfAlert) {
        this.durationOfAlert = durationOfAlert;
    }

    public @NotNull(message = "Active must be set to true or false.") Boolean getisActive() {
        return isActive;
    }

    public void setisActive(@NotNull(message = "Active must be set to true or false.") Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
