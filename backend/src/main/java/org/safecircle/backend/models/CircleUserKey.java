package org.safecircle.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CircleUserKey {

    @Column(name = "circle_id")
    private long circleId;

    @Column(name = "user_id")
    private long userId;

    protected CircleUserKey() {}

    public CircleUserKey(long circleId, long userId) {
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

    public long getCircleId() {
        return circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
