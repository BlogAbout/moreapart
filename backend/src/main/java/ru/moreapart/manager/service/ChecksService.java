package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.moreapart.manager.dto.ChecksAnswersPageDto;
import ru.moreapart.manager.dto.ChecksPageDto;
import ru.moreapart.manager.entity.Checks;
import ru.moreapart.manager.entity.ChecksAnswers;
import ru.moreapart.manager.entity.ChecksQuestions;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.repository.ChecksAnswersRepository;
import ru.moreapart.manager.repository.ChecksRepository;
import ru.moreapart.manager.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChecksService {
    private final ChecksRepository checksRepository;
    private final ChecksAnswersRepository checksAnswersRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChecksService(ChecksRepository checksRepository, ChecksAnswersRepository checksAnswersRepository, UserRepository userRepository) {
        this.checksRepository = checksRepository;
        this.checksAnswersRepository = checksAnswersRepository;
        this.userRepository = userRepository;
    }

    public Checks create(Checks checks, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        checks.setAuthor(user);
        checks.setDateCreated(LocalDateTime.now());
        checks.setDateModify(LocalDateTime.now());

        Checks updatable = checksRepository.save(checks);
        for (ChecksQuestions checksQuestions : updatable.getChecksQuestions()) {
            checksQuestions.setCheck(updatable.getId());
        }
        updatable = checksRepository.save(updatable);

        return updatable;
    }

    public Checks update(Checks checksFromDb, Checks checks) throws IOException {
        checksFromDb.setTitle(checks.getTitle());
        checksFromDb.setDescription(checks.getDescription());
        checksFromDb.setDateModify(LocalDateTime.now());
        checksFromDb.setRights(checks.getRights());
        checksFromDb.setChecksQuestions(checks.getChecksQuestions());

        return checksRepository.save(checksFromDb);
    }

    public void delete(Checks checks) {
        checksRepository.delete(checks);
    }

    public Checks findById(Long id) {
        return checksRepository.findById(id).orElseThrow(null);
    }

    public List<Checks> findAll() {
        return checksRepository.findAll();
    }

    public ChecksPageDto findAll(Pageable pageable) {
        Page<Checks> page = checksRepository.findAll(pageable);

        return new ChecksPageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    public ChecksAnswers fill(ChecksAnswers checksAnswers, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        checksAnswers.setAuthor(user);
        checksAnswers.setDateCreated(LocalDateTime.now());

        return checksAnswersRepository.save(checksAnswers);
    }

    public ChecksAnswersPageDto findAllReports(Pageable pageable) {
        Page<ChecksAnswers> page = checksAnswersRepository.findAll(pageable);

        for (ChecksAnswers checksAnswers : page) {
            checksAnswers.setChecksInfo(checksRepository.findById(checksAnswers.getCheck()).orElseThrow(null));
        }

        return new ChecksAnswersPageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}