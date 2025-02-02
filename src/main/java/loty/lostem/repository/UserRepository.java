package loty.lostem.repository;

import loty.lostem.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByTag(String tag);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmail(String email);

    boolean existsByTag(String tag);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
