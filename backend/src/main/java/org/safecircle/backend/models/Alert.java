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
import org.safecircle.backend.enums.SafetyStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.")
    @Column(name = "alert")
    private SafetyStatus alert;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters")
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<CircleAlert> circleAlerts;

    @OneToMany(mappedBy = "circle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<UserAlert> userAlerts;

    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    @JsonBackReference
    private Location location;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime creatadAt;

    protected Alert() {}

    public Alert(SafetyStatus alert, String description) {
        this.alert = alert;
        this.description = description;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public @NotBlank(message = "Description is required") @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description is required") @Size(min = 5, max = 40, message = "Description must be between 5 and 40 characters") String description) {
        this.description = description;
    }

    public LocalDateTime getCreatadAt() {
        return creatadAt;
    }

    public void setCreatadAt(LocalDateTime creatadAt) {
        this.creatadAt = creatadAt;
    }

    public @NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.") SafetyStatus getAlert() {
        return alert;
    }

    public void setAlert(@NotNull(message = "Safety Status required. Values are: SOS, UNSAFE.") SafetyStatus alert) {
        this.alert = alert;
    }
}
