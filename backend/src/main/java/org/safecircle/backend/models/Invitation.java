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
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

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
}
