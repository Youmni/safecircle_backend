package org.safecircle.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAlertKey implements Serializable {
    @Column(name = "user_id")
    private long userId;

    @Column(name = "alert_id")
    private long alertId;

    protected UserAlertKey() {
    }

    public UserAlertKey(long userId, long alertId) {
        this.userId = userId;
        this.alertId = alertId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        UserAlertKey that = (UserAlertKey)obj;
        return Objects.equals(userId, that.userId) && Objects.equals(alertId, that.alertId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, alertId);
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getAlertId() {
        return alertId;
    }

    public void setAlertId(long alertId) {
        this.alertId = alertId;
    }
}
