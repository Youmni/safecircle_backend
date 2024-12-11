package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Circle;
import org.safecircle.backend.models.CircleUser;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CircleUserRepository extends JpaRepository<CircleUser, Long> {

    List<CircleUser> findByCircle(Circle circle);
    List<CircleUser> findByUser(User user);
    List<CircleUser> findByUserAndCircle(User user, Circle circle);
    List<CircleUser> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<CircleUser> findByCreatedAtBefore(LocalDateTime before);
    List<CircleUser> findByCreatedAtAfter(LocalDateTime after);

    boolean existsByCircle(Circle circle);
    boolean existsByUser(User user);
}
