package org.safecircle.backend.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.safecircle.backend.enums.InvitationStatus;

import java.time.LocalDateTime;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invitationId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The invitation status is required. Accepted values are: ACCEPTED, DECLINED, PENDING")
    private InvitationStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @NotNull(message = "Sender is required")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @NotNull(message = "Receiver is required")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "circle_id", nullable = false)
    @NotNull(message = "Circle is required")
    private Circle circle;

    protected Invitation() {
    }

    public Invitation(InvitationStatus status) {
        this.status = status;
    }

    public @NotNull(message = "The invitation status is required. Accepted values are: ACCEPTED, DECLINED, PENDING") InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "The invitation status is required. Accepted values are: ACCEPTED, DECLINED, PENDING") InvitationStatus status) {
        this.status = status;
    }

    public long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(long invitationId) {
        this.invitationId = invitationId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
