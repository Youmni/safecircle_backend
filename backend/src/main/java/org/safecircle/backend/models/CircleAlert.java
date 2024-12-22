package org.safecircle.backend.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class CircleAlert {

    @EmbeddedId
    private CircleAlertKey id;

    @ManyToOne
    @MapsId("circleId")
    @JoinColumn(name = "circle_id")
    @NotNull(message = "Circle cannot be null")
    private Circle circle;

    @ManyToOne
    @MapsId("alertId")
    @JoinColumn(name = "alert_id")
    @NotNull(message = "Alert cannot be null")
    private Alert alert;

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

    protected CircleAlert() {}

    public CircleAlert(Alert alert, Circle circle) {
        this.alert = alert;
        this.circle = circle;
        this.id = new CircleAlertKey(circle.getCircleId(), alert.getAlertId());
    }

    public CircleAlertKey getId() {
        return id;
    }

    public void setId(CircleAlertKey id) {
        this.id = id;
    }

    public @NotNull(message = "Circle cannot be null") Circle getCircle() {
        return circle;
    }

    public void setCircle(@NotNull(message = "Circle cannot be null") Circle circle) {
        this.circle = circle;
    }

    public @NotNull(message = "Alert cannot be null") Alert getAlert() {
        return alert;
    }

    public void setAlert(@NotNull(message = "Alert cannot be null") Alert alert) {
        this.alert = alert;
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
}
