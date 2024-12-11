package org.safecircle.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.safecircle.backend.enums.InvitationStatus;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long invitationId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The invitation status is required. Accepted values are: ACCEPTED, DECLINED, PENDING")
    private InvitationStatus status;

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
