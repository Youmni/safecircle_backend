package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
}
