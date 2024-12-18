package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private long locationId;

    @NotNull(message = "You need to provide a latitude")
    @DecimalMin(value = "-90", message = "Latitude cannot be smaller than -90")
    @DecimalMax(value = "90", message = "Latitude cannot be bigger than 90")
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    @NotNull(message = "You need to provide a longitude")
    @DecimalMin(value = "-180", message = "Longitude cannot be smaller than -180")
    @DecimalMax(value = "180", message = "Longitude cannot be bigger than 180")
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;


    @CreationTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "location")
    @JsonManagedReference("alert-location")
    private Alert alert;


    public Location() {
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

    public @NotNull(message = "You need to provide a latitude") @DecimalMin(value = "-90", message = "Latitude cannot be smaller than -90") @DecimalMax(value = "90", message = "Latitude cannot be bigger than 90") BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull(message = "You need to provide a latitude") @DecimalMin(value = "-90", message = "Latitude cannot be smaller than -90") @DecimalMax(value = "90", message = "Latitude cannot be bigger than 90") BigDecimal latitude) {
        this.latitude = latitude;
    }

    public @NotNull(message = "You need to provide a longitude") @DecimalMin(value = "-90", message = "Longitude cannot be smaller than -90") @DecimalMax(value = "90", message = "Longitude cannot be bigger than 90") BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull(message = "You need to provide a longitude") @DecimalMin(value = "-90", message = "Longitude cannot be smaller than -90") @DecimalMax(value = "90", message = "Longitude cannot be bigger than 90") BigDecimal longitude) {
        this.longitude = longitude;
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

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
}
