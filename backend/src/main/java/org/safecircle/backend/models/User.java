package org.safecircle.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.safecircle.backend.enums.UserType;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @NotBlank
    @Size(min = 2, max = 50, message = "First name must be between 5 and 15 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50, message = "Last name must be between 5 and 15 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Email must be valid")
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Password cannot be empty or null")
    @Column(name = "password")
    private String password;

    @Column(name = "phone_number")
    private long phoneNumber;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Usertype should be given. Option are: ADMIN, USER")
    private UserType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Blacklist> blacklists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> invitations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<UserAlert> userAlerts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    Set<CircleUser> circleUsers;

    @ManyToOne
    @JoinColumn(name = "location", nullable = false)
    @JsonBackReference
    private Location location;

    @CreationTimestamp
    @Column(name = "created_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    protected User() {}

    public User(String firstName, String lastName, String email, String password, long phoneNumber, UserType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public @NotBlank @Size(min = 2, max = 50, message = "First name must be between 5 and 15 characters") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank @Size(min = 2, max = 50, message = "First name must be between 5 and 15 characters") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank @Size(min = 2, max = 50, message = "Last name must be between 5 and 15 characters") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank @Size(min = 2, max = 50, message = "Last name must be between 5 and 15 characters") String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be empty or null") @Email(message = "Email must be valid") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password cannot be empty or null") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be empty or null") String password) {
        this.password = password;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotBlank(message = "Usertype should be given. Option are: ADMIN, USER") UserType getType() {
        return type;
    }

    public void setType(@NotBlank(message = "Usertype should be given. Option are: ADMIN, USER") UserType type) {
        this.type = type;
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
