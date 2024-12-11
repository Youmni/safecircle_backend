package org.safecircle.backend.repositories;

import org.safecircle.backend.models.CircleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircleUserRepository extends JpaRepository<CircleUser, Long> {
}
