package org.safecircle.backend.repositories;

import org.safecircle.backend.models.UserAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAlertRepository extends JpaRepository<UserAlert, Long> {
}
