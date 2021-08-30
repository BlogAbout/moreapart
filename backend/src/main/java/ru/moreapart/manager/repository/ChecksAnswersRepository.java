package ru.moreapart.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moreapart.manager.entity.ChecksAnswers;

import java.util.Optional;

@Repository
public interface ChecksAnswersRepository extends JpaRepository<ChecksAnswers, Long> {
    Optional<ChecksAnswers> findById(Long id);
}