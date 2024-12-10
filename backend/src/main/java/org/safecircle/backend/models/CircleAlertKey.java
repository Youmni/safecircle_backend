package org.safecircle.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CircleAlertKey {

    @Column(name = "circle_id")
    private Long circleId;

    @Column(name = "alert_id")
    private Long alertId;

    protected CircleAlertKey() {}

    public CircleAlertKey(Long circleId, Long alertId) {
        this.circleId = circleId;
        this.alertId = alertId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleAlertKey that = (CircleAlertKey) o;
        return circleId == that.circleId && alertId == that.alertId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(circleId, alertId);
    }

    public Long getCircleId() {
        return circleId;
    }

    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }
}
