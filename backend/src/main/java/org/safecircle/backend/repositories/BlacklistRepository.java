package org.safecircle.backend.repositories;

import org.safecircle.backend.models.Blacklist;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    List<Blacklist> findByBlacklistId(long blacklistId);
    List<Blacklist> findByUser(User user);
    List<Blacklist> findByUserAndBlacklistId(User user, long blacklistId);
    List<Blacklist> findByStartDate(LocalDate startDate);
    List<Blacklist> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<Blacklist> findByStartDateAfter(LocalDate startDate);
    List<Blacklist> findByStartDateBefore(LocalDate startDate);
    List<Blacklist> findByEndDate(LocalDate endDate);
    List<Blacklist> findByEndDateAfter(LocalDate endDate);
    List<Blacklist> findByEndDateBefore(LocalDate endDate);
    List<Blacklist> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByBlacklistId(long blacklistId);
    boolean existsByUser(User user);
}
