package org.safecircle.backend.dto;

import org.safecircle.backend.enums.ReportStatus;
import org.safecircle.backend.models.Report;
import org.safecircle.backend.models.User;

public class ReportDTO {
    private String subject;
    private String description;
    private String email;
    private ReportStatus status;
    private long userId;

    public ReportDTO(String subject, String description, String email, ReportStatus status) {
        this.subject = subject;
        this.description = description;
        this.email = email;
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
