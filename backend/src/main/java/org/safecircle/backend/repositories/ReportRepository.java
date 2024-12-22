package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.ReportStatus;
import org.safecircle.backend.models.Report;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReportId(long reportId);
    List<Report> findAllByOrderByReportIdAsc();
    List<Report> findAllByOrderByReportIdDesc();

    List<Report> findBySubject(String subject);

    List<Report> findByEmail(String email);
    List<Report> findByEmailContaining(String email);


    List<Report> findByStatus(ReportStatus status);

    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Report> findByCreatedAtBefore(LocalDateTime start);
    List<Report> findByCreatedAtAfter(LocalDateTime end);

    List<Report> findByUser(User user);

    boolean existsByReportId(long reportId);
    boolean existsByEmail(String subject);
}
