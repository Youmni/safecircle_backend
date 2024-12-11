package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long eventId;

    @NotEmpty(message = "You need to provide an estimation of the amount of attendees")
    @Size(min = 0, message = "There must be positive amount of attendees")
    private int userCountEstimate;

    @NotEmpty(message = "You need to provide an email of the coordinator of the event")
    @Email(message = "The email must be valid")
    private String email;

    @NotEmpty(message = "You need to provide the name of the event")
    @Size(min = 2, max = 50, message = "The event name needs to be between 2 and 50 characters long")
    private String eventName;

    @NotEmpty(message = "You need to provide a start time for the event")
    @Future(message = "The start date needs to be in the future")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startDate;

    @NotEmpty(message = "You need to provide a end time for the event")
    @Future(message = "The end date needs to be in the future")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    @JsonBackReference
    private Location location;

    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    protected Event() {
    }

    public Event(int userCountEstimate, String eventName, String email, LocalDate startDate, LocalDate endDate) {
        this.userCountEstimate = userCountEstimate;
        this.eventName = eventName;
        this.email = email;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public @NotEmpty(message = "You need to provide a end time for the event") @Future(message = "The end date needs to be in the future") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotEmpty(message = "You need to provide a end time for the event") @Future(message = "The end date needs to be in the future") LocalDate endDate) {
        this.endDate = endDate;
    }

    public @NotEmpty(message = "You need to provide a start time for the event") @Future(message = "The start date needs to be in the future") LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotEmpty(message = "You need to provide a start time for the event") @Future(message = "The start date needs to be in the future") LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotEmpty(message = "You need to provide the name of the event") @Size(min = 2, max = 50, message = "The event name needs to be between 2 and 50 characters long") String getEventName() {
        return eventName;
    }

    public void setEventName(@NotEmpty(message = "You need to provide the name of the event") @Size(min = 2, max = 50, message = "The event name needs to be between 2 and 50 characters long") String eventName) {
        this.eventName = eventName;
    }

    public @NotEmpty(message = "You need to provide an email of the coordinator of the event") @Email(message = "The email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "You need to provide an email of the coordinator of the event") @Email(message = "The email must be valid") String email) {
        this.email = email;
    }

    @NotEmpty(message = "You need to provide an estimation of the amount of attendees")
    @Size(min = 0, message = "There must be positive amount of attendees")
    public int getUserCountEstimate() {
        return userCountEstimate;
    }

    public void setUserCountEstimate(@NotEmpty(message = "You need to provide an estimation of the amount of attendees") @Size(min = 0, message = "There must be positive amount of attendees") int userCountEstimate) {
        this.userCountEstimate = userCountEstimate;
    }
}