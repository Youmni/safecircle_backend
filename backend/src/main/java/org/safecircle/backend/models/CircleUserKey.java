package org.safecircle.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CircleUserKey {

    @Column(name = "circle_id")
    private Long circleId;

    @Column(name = "user_id")
    private Long userId;

    protected CircleUserKey() {}

    public CircleUserKey(Long circleId, Long userId) {
        this.circleId = circleId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleUserKey that = (CircleUserKey) o;
        return circleId == that.circleId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(circleId, userId);
    }

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
