package org.safecircle.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.safecircle.backend.enums;

@Entity
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The invitation status is required. Accepted values are: ACCEPTED, DECLINED, PENDING")
    private InvitationStatus status;

    protected Invitation() {
    }

    public Invitation(InvitationStatus status) {
        this.status = status;
    }
}
