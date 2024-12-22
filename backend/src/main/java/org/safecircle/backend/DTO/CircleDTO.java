package org.safecircle.backend.dto;

import org.safecircle.backend.enums.CircleType;

public class CircleDTO {
    private String CircleName;
    private CircleType CircleType;
    private boolean available;
    private long circleId;

    public CircleDTO() {
    }

    public CircleDTO(String circleName, org.safecircle.backend.enums.CircleType circleType, boolean available) {
        CircleName = circleName;
        CircleType = circleType;
        this.available = available;
    }

    public CircleDTO(String circleName, org.safecircle.backend.enums.CircleType circleType, boolean available, long circleId) {
        CircleName = circleName;
        CircleType = circleType;
        this.available = available;
        this.circleId = circleId;
    }

    public String getCircleName() {
        return CircleName;
    }

    public void setCircleName(String circleName) {
        CircleName = circleName;
    }

    public org.safecircle.backend.enums.CircleType getCircleType() {
        return CircleType;
    }

    public void setCircleType(org.safecircle.backend.enums.CircleType circleType) {
        CircleType = circleType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }
}
