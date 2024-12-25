package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.SafetyStatus;
import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByAlertId(long alertId);
    List<Alert> findByStatus(SafetyStatus status);
    List<Alert> findByStatusIn(List<SafetyStatus> statuses);
    List<Alert> findByStatusAndAlertId(SafetyStatus status, Long alertId);
    List<Alert> findByLocation(Location location);
    List<Alert> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Alert> findByCreatedAtBefore(LocalDateTime before);
    List<Alert> findByCreatedAtAfter(LocalDateTime after);
    List<Alert> findByUser(User user);
    List<Alert> findByUserAndAlertId(User user, Long alertId);
    List<Alert> findByIsActive(boolean isActive);

    boolean existsByAlertId(long alertId);
}
