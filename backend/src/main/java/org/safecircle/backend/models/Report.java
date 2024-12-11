package org.safecircle.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.safecircle.backend.enums.ReportStatus;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;

    @NotEmpty(message = "You need to provide a subject for the report")
    @Size(min = 2, max = 40, message = "The subject needs to be between 2 and 40 characters long")
    private String subject;

    @NotEmpty(message = "You need to provide a description")
    @Size(min = 16, max = 512, message = "The description needs to be between 16 and 512 characters long")
    private String description;

    @NotEmpty(message = "You need to provide the email of the sender")
    @Email(message = "The email must be valid")
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "The report status is required. Accepted values are: PENDING, RESOLVED, CLOSED, FLAGGED")
    private ReportStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    protected Report() {
    }

    public Report(String subject, String description, String email, ReportStatus status) {
        this.subject = subject;
        this.description = description;
        this.email = email;
        this.status = status;
    }

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public @NotNull(message = "The report status is required. Accepted values are: PENDING, RESOLVED, CLOSED, FLAGGED") ReportStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "The report status is required. Accepted values are: PENDING, RESOLVED, CLOSED, FLAGGED") ReportStatus status) {
        this.status = status;
    }

    public @NotEmpty(message = "You need to provide the email of the sender") @Email(message = "The email must be valid") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "You need to provide the email of the sender") @Email(message = "The email must be valid") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "You need to provide a description") @Size(min = 16, max = 512, message = "The description needs to be between 16 and 512 characters long") String getDescription() {
        return description;
    }

    public void setDescription(@NotEmpty(message = "You need to provide a description") @Size(min = 16, max = 512, message = "The description needs to be between 16 and 512 characters long") String description) {
        this.description = description;
    }

    public @NotEmpty(message = "You need to provide a subject for the report") @Size(min = 2, max = 40, message = "The subject needs to be between 2 and 40 characters long") String getSubject() {
        return subject;
    }

    public void setSubject(@NotEmpty(message = "You need to provide a subject for the report") @Size(min = 2, max = 40, message = "The subject needs to be between 2 and 40 characters long") String subject) {
        this.subject = subject;
    }
}
