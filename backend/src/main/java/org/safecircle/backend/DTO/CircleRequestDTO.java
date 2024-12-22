package org.safecircle.backend.dto;

import org.safecircle.backend.enums.CircleType;

import java.time.LocalDateTime;

public class CircleRequestDTO {

    private long circleId;
    private String CircleName;
    private org.safecircle.backend.enums.CircleType CircleType;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public CircleRequestDTO(long circleId, String circleName, org.safecircle.backend.enums.CircleType circleType, boolean available, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.circleId = circleId;
        CircleName = circleName;
        CircleType = circleType;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public org.safecircle.backend.enums.CircleType getCircleType() {
        return CircleType;
    }

    public void setCircleType(org.safecircle.backend.enums.CircleType circleType) {
        CircleType = circleType;
    }

    public String getCircleName() {
        return CircleName;
    }

    public void setCircleName(String circleName) {
        CircleName = circleName;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
