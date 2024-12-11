package org.safecircle.backend.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long locationId;

    @NotEmpty(message = "You need to provide a latitude")
    @Size(min = -90, max = 90, message = "The latitude cannot be bigger than 90 or smaller than -90")
    private BigDecimal latitude;

    @NotEmpty(message = "You need to provide a longitude")
    @Size(min = -90, max = 90, message = "The longitude cannot be bigger than 90 or smaller than -90")
    private BigDecimal longitude;

    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    protected Location() {
    }

    public Location(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
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

    public @NotEmpty(message = "You need to provide a longitude") @Size(min = -90, max = 90, message = "The longitude cannot be bigger than 90 or smaller than -90") BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotEmpty(message = "You need to provide a longitude") @Size(min = -90, max = 90, message = "The longitude cannot be bigger than 90 or smaller than -90") BigDecimal longitude) {
        this.longitude = longitude;
    }

    public @NotEmpty(message = "You need to provide a latitude") @Size(min = -90, max = 90, message = "The latitude cannot be bigger than 90 or smaller than -90") BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotEmpty(message = "You need to provide a latitude") @Size(min = -90, max = 90, message = "The latitude cannot be bigger than 90 or smaller than -90") BigDecimal latitude) {
        this.latitude = latitude;
    }
}