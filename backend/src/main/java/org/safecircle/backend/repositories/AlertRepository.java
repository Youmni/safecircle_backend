package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {


}
