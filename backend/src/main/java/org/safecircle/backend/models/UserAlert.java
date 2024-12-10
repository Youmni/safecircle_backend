package org.safecircle.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class UserAlert {
    @EmbeddedId
    private UserAlertKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    private User user;

    @ManyToOne
    @MapsId("alertId")
    @JoinColumn(name = "alert_id")
    @NotNull(message = "Alert is required")
    private Alert alert;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    protected UserAlert() {
    }

    public UserAlert(User user, Alert alert) {
        this.user = user;
        this.alert = alert;
    }

    public UserAlertKey getId() {
        return id;
    }

    public void setId(UserAlertKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
