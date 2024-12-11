package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import org.safecircle.backend.enums.CircleType;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Circle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cirlce_id")
    private long circleId;

    @NotBlank(message = "Circle name required")
    @Size(min = 4, max = 50, message = "A circle name needs to be between 4 and 50 characters")
    @Column(name = "circle_name")
    private String circleName;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Circle type required. Could be: REGULAR, EVENT")
    @Column(name = "circle_type")
    private CircleType circleType;

    @NotNull(message = "Availibilty should not be null")
    @Column(name = "is_available")
    private boolean isAvailable;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<CircleAlert> circleAlerts;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<CircleUser> circleUsers;

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

    protected Circle() {}

    public Circle(CircleType circleType, boolean isAvailable, String circleName) {
        this.circleType = circleType;
        this.isAvailable = isAvailable;
        this.circleName = circleName;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public @NotBlank(message = "Circle name required") @Size(min = 4, max = 50, message = "A circle name needs to be between 4 and 50 characters") String getCircleName() {
        return circleName;
    }

    public void setCircleName(@NotBlank(message = "Circle name required") @Size(min = 4, max = 50, message = "A circle name needs to be between 4 and 50 characters") String circleName) {
        this.circleName = circleName;
    }

    public @NotBlank(message = "Circle type required. Could be: REGULAR, EVENT") CircleType getCircleType() {
        return circleType;
    }

    public void setCircleType(@NotBlank(message = "Circle type required. Could be: REGULAR, EVENT") CircleType circleType) {
        this.circleType = circleType;
    }

    @NotNull(message = "Availibilty should not be null")
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(@NotNull(message = "Availibilty should not be null") boolean available) {
        isAvailable = available;
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
