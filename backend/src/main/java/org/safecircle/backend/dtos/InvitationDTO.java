package org.safecircle.backend.dtos;

import org.safecircle.backend.enums.InvitationStatus;

import java.time.LocalDateTime;

public class InvitationDTO {
    private long invitationId;
    private InvitationStatus status;
    private LocalDateTime createdAt;
    private long senderId;
    private long receiverId;
    private long circleId;
    private String circleName;

    public InvitationDTO() {
    }

    public InvitationDTO(InvitationStatus status, long senderId, long receiverId, long circleId, String circleName) {
        this.status = status;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.circleId = circleId;
        this.circleName = circleName;
    }

    public long getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(long invitationId) {
        this.invitationId = invitationId;
    }

    public InvitationStatus getStatus() {
        return status;
    }

    public void setStatus(InvitationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
}
