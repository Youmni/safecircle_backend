package org.safecircle.backend.repositories;

import jakarta.validation.constraints.NotNull;
import org.safecircle.backend.enums.InvitationStatus;
import org.safecircle.backend.models.Invitation;
import org.safecircle.backend.models.Location;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByInvitationId(long invitationId);
    List<Invitation> findAllByOrderByInvitationIdAsc();
    List<Invitation> findAllByOrderByInvitationIdDesc();

    List<Invitation> findByStatus(InvitationStatus status);

    List<Invitation> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<Invitation> findByCreatedAtBefore(LocalDateTime time);
    List<Invitation> findByCreatedAtAfter(LocalDateTime time);

    List<Invitation> findInvitationsByReceiver(User receiver);

    boolean existsByInvitationId(long invitationId);
}
