package ru.moreapart.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moreapart.manager.entity.Rights;
import ru.moreapart.manager.entity.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByResetKey(String key);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByResetKey(String key);

    int countByRights(Rights rights);

    @Transactional
    void deleteByUsername(@Param("username") String username);
}