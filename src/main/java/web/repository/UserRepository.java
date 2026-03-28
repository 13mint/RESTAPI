package web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web.model.AppUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}
