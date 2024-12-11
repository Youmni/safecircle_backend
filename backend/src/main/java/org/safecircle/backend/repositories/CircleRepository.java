package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.CircleType;
import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.CircleAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CircleRepository extends JpaRepository<Circle, Long> {
    List<Circle> findByCircleId(long id);
    List<Circle> findByCircleName(String name);
    List<Circle> findByCircleType(CircleType type);
    List<Circle> findByAvailable(boolean available);

    List<Circle> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Circle> findByCreatedAtBefore(LocalDateTime createdAt);
    List<Circle> findByCreatedAtAfter(LocalDateTime createdAt);

    boolean existsByCircleId(long id);
    boolean existsByCircleName(String name);
    boolean existsByCircleType(CircleType type);
}
