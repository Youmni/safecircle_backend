package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Circle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleRepository extends JpaRepository<Circle, Long> {
}
