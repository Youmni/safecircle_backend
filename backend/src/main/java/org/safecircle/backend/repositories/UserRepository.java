package org.safecircle.backend.repositories;

import org.safecircle.backend.enums.UserType;
import org.safecircle.backend.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserId(long userId);
    List<User> findAllByOrderByUserIdAsc(long userId);
    List<User> findAllByOrderByUserIdDesc(long userId);

    List<User> findByFirstName(String firstName);
    List<User> findAllByOrderByFirstNameAsc(String firstName);
    List<User> findAllByOrderByFirstNameDesc(String firstName);
    List<User> findByFirstNameContainingIgnoreCase(String firstName);

    List<User> findByLastName(String lastName);
    List<User> findAllByOrderByLastNameAsc(String lastName);
    List<User> findAllByOrderByLastNameDesc(String lastName);
    List<User> findByLastNameContainingIgnoreCase(String lastName);

    List<User> findByEmail(String email);
    List<User> findAllByOrderByEmailAsc(String email);
    List<User> findAllByOrderByEmailDesc(String email);
    List<User> findByEmailContainingIgnoreCase(String email);

    List<User> findByPhoneNumber(long phoneNumber);
    List<User> findByType(UserType userType);

    List<User> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<User> findByCreatedAtBefore(LocalDateTime time);
    List<User> findByCreatedAtAfter(LocalDateTime time);
    List<User> findByUpdatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<User> findByUpdatedAtBefore(LocalDateTime time);
    List<User> findByUpdatedAtAfter(LocalDateTime time);

    boolean existsByUserId(long userId);
    boolean existsByEmail(String email);
}
