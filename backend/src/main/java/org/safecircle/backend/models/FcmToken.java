package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "fcm_token")
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fcm_token_id")
    private long fcmTokenId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User is required")
    @JsonManagedReference("fcmToken-user")
    private User user;

    @Column(name = "fcm_token")
    private String fcmToken;

    protected FcmToken() {}

    public FcmToken(User user, String fcmToken) {
        this.user = user;
        this.fcmToken = fcmToken;
    }


    public long getFcmTokenId() {
        return fcmTokenId;
    }

    public void setFcmTokenId(long fcmTokenId) {
        this.fcmTokenId = fcmTokenId;
    }

    public @NotNull(message = "User is required") User getUser() {
        return user;
    }

    public void setUser(@NotNull(message = "User is required") User user) {
        this.user = user;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
