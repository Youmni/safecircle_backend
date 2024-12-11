package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByLocationId(long locationId);
    List<Location> findAllByOrderByLocationIdAsc(long locationId);
    List<Location> findAllByOrderByLocationIdDesc(long locationId);

    List<Location> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Location> findByCreatedAtBefore(LocalDateTime time);
    List<Location> findByCreatedAtAfter(LocalDateTime time);
    List<Location> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Location> findByUpdatedAtBefore(LocalDateTime time);
    List<Location> findByUpdatedAtAfter(LocalDateTime time);

    boolean existsByLocationId(long locationId);
}
