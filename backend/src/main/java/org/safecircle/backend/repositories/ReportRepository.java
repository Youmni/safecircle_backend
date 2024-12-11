package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.ReportStatus;
import org.safecircle.backend.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByReportId(long reportId);
    List<Report> findAllByOrderByReportIdAsc(long reportId);
    List<Report> findAllByOrderByReportIdDesc(long reportId);

    List<Report> findAllBySubject(String subject);

    List<Report> findAllByEmail(String email);

    List<Report> findAllByStatus(ReportStatus status);

    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Report> findByCreatedAtBefore(LocalDateTime start);
    List<Report> findByCreatedAtAfter(LocalDateTime end);

    boolean existsByReportId(long reportId);
    boolean existsByEmail(String subject);
}
