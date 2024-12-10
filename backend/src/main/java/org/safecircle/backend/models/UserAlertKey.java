package org.safecircle.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAlertKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "alert_id")
    private Long alertId;

    protected UserAlertKey() {
    }

    public UserAlertKey(Long userId, Long alertId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }
}
