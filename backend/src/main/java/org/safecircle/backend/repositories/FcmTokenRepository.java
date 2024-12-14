package org.safecircle.backend.repositories;

import org.safecircle.backend.models.FcmToken;
import org.safecircle.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByFcmTokenId(long fcmTokenId);
    List<FcmToken> findByUserAndFcmTokenId(User user, long fcmTokenId);
    List<FcmToken> findByUser(User user);
}
