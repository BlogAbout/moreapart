package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moreapart.manager.entity.Checks;
import ru.moreapart.manager.entity.ChecksQuestions;
import ru.moreapart.manager.repository.ChecksQuestionsRepository;

import java.io.IOException;
import java.util.List;

@Service
public class ChecksQuestionsService {
    private final ChecksQuestionsRepository checksQuestionsRepository;

    @Autowired
    public ChecksQuestionsService(ChecksQuestionsRepository checksQuestionsRepository) {
        this.checksQuestionsRepository = checksQuestionsRepository;
    }

    public ChecksQuestions create(ChecksQuestions checksQuestions) throws IOException {
        return checksQuestionsRepository.save(checksQuestions);
    }

    public ChecksQuestions update(ChecksQuestions checksQuestionsFromDb, ChecksQuestions checksQuestions) throws IOException {
        checksQuestionsFromDb.setTitle(checksQuestions.getTitle());
        checksQuestionsFromDb.setCategory(checksQuestions.getCategory());
        checksQuestionsFromDb.setType(checksQuestions.getType());
        checksQuestionsFromDb.setValues(checksQuestions.getValues());

        return checksQuestionsRepository.save(checksQuestionsFromDb);
    }

    public void delete(ChecksQuestions checks) {
        checksQuestionsRepository.delete(checks);
    }

    public List<ChecksQuestions> findAllByChecks(Checks checks) {
        return checksQuestionsRepository.findAllByCheck(checks.getId());
    }
}