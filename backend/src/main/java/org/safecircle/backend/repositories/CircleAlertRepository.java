package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.CircleAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CircleAlertRepository extends JpaRepository<CircleAlert, Long> {

    List<CircleAlert> findByCircle(Circle circle);
    List<CircleAlert> findByAlert(Alert alert);
    List<CircleAlert> findByCircleAndAlert(Circle circle, Alert alert);
    List<CircleAlert> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<CircleAlert> findByCreatedAtBefore(LocalDateTime createdAt);
    List<CircleAlert> findByCreatedAtAfter(LocalDateTime createdAt);

    boolean existsByCircle(Circle circle);
    boolean existsByAlert(Alert alert);
}
