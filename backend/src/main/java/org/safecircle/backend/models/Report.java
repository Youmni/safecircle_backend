package org.safecircle.backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.safecircle.backend.enums;

@Entity
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

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

    protected Report() {
    }

    public Report(String subject, String description, String email, ReportStatus status) {
        this.subject = subject;
        this.description = description;
        this.email = email;
        this.status = status;
    }
}
