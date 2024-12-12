package org.safecircle.backend.dto;

import org.safecircle.backend.enums.CircleType;

public class CircleDTO {
    private String circleName;
    private CircleType circleType;
    private boolean isAvailable;

    public CircleDTO() {}

    public CircleDTO(String circleName, CircleType circleType, boolean isAvailable) {
        this.circleName = circleName;
        this.circleType = circleType;
        this.isAvailable = isAvailable;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public CircleType getCircleType() {
        return circleType;
    }

    public void setCircleType(CircleType circleType) {
        this.circleType = circleType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
