package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Alert;
import org.safecircle.backend.models.User;
import org.safecircle.backend.models.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {
    List<UserAlert> findByUser(User user);
    List<UserAlert> findByAlert(Alert alert);
    List<UserAlert> findByUserAndAlert(User user, Alert alert);
    List<UserAlert> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<UserAlert> findByCreatedAtBefore(LocalDateTime time);
    List<UserAlert> findByCreatedAtAfter(LocalDateTime time);
}
