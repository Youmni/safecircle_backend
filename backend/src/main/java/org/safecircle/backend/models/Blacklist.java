package org.safecircle.backend.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklist_id")
    private long blacklistId;

    @NotBlank
    @Size(min = 8, max = 50, message = "Reason of the blacklist")
    @Column(name = "decription")
    private String description;

    @NotNull(message = "A start date must be given")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull(message = "A end date must be given")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    protected Blacklist() {}

    public Blacklist(String description, LocalDate startDate, LocalDate endDate, User user) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }

    public long getBlacklistId() {
        return blacklistId;
    }

    public void setBlacklistId(long blacklistId) {
        this.blacklistId = blacklistId;
    }

    public @NotBlank @Size(min = 8, max = 50, message = "Reason of the blacklist") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank @Size(min = 8, max = 50, message = "Reason of the blacklist") String description) {
        this.description = description;
    }

    public @NotNull(message = "A start date must be given") LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NotNull(message = "A start date must be given") LocalDate startDate) {
        this.startDate = startDate;
    }

    public @NotNull(message = "A end date must be given") LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(@NotNull(message = "A end date must be given") LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
