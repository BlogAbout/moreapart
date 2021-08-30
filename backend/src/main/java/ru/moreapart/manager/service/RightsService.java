package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moreapart.manager.entity.Rights;
import ru.moreapart.manager.repository.RightsRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RightsService {
    private final RightsRepository rightsRepository;

    @Autowired
    public RightsService(RightsRepository rightsRepository) {
        this.rightsRepository = rightsRepository;
    }

    public Rights create(Rights rights, Long userId) throws IOException {
        rights.setUserId(userId);
        rights.setDateCreated(LocalDateTime.now());
        rights.setDateModify(LocalDateTime.now());
        rights.setCountPersons(0);

        return rightsRepository.save(rights);
    }

    public Rights update(Rights rightsFromDb, Rights rights) throws IOException {
        rightsFromDb.setTitle(rights.getTitle());
        rightsFromDb.setDescription(rights.getDescription());
        rightsFromDb.setGranted(rights.getGranted());
        rightsFromDb.setSiblings(rights.getSiblings());
        rightsFromDb.setDateModify(LocalDateTime.now());

        return rightsRepository.save(rightsFromDb);
    }

    public void delete(Rights rights) {
        rightsRepository.deleteSiblingsByRoleId(rights.getId());
        rightsRepository.delete(rights);
    }

    public int updateCountPersons(Rights rights, int count) {
        rights.setCountPersons(count);
        rightsRepository.save(rights);

        return 0;
    }

    public List<Rights> findAll() {
        return rightsRepository.findAll();
    }

    public Rights findById(Long id) {
        return rightsRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Ошибка. Роль не найдена."));
    }
}