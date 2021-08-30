package ru.moreapart.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moreapart.manager.entity.Checks;

import java.util.Optional;

@Repository
public interface ChecksRepository extends JpaRepository<Checks, Long> {
    Optional<Checks> findById(Long id);
}