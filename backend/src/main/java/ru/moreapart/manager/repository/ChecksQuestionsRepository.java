package ru.moreapart.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moreapart.manager.entity.ChecksQuestions;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecksQuestionsRepository extends JpaRepository<ChecksQuestions, Long> {
    Optional<ChecksQuestions> findById(Long id);

    List<ChecksQuestions> findAllByCheck(Long id);
}