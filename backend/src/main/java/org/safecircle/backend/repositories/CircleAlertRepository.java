package org.safecircle.backend.repositories;

import org.safecircle.backend.models.CircleAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleAlertRepository extends JpaRepository<CircleAlert, Long> {
}
