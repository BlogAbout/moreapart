package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.moreapart.manager.dto.InventoryPageDto;
import ru.moreapart.manager.dto.PageDto;
import ru.moreapart.manager.entity.Inventory;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.repository.InventoryRepository;
import ru.moreapart.manager.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final UploaderService uploaderService;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, UserRepository userRepository, UploaderService uploaderService) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.uploaderService = uploaderService;
    }

    public Inventory create(Inventory inventory, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        inventory.setAuthor(user);
        inventory.setDateCreated(LocalDateTime.now());
        inventory.setDateModify(LocalDateTime.now());

        return inventoryRepository.save(inventory);
    }

    public Inventory create(Inventory inventory, MultipartFile file, Long userId) throws IOException {
        String fileName = uploaderService.save(file, "equipment", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        inventory.setAuthor(user);
        inventory.setDateCreated(LocalDateTime.now());
        inventory.setDateModify(LocalDateTime.now());
        inventory.setImage(fileName);

        return inventoryRepository.save(inventory);
    }

    public void create(MultipartFile file) throws IOException {

    }

    public Inventory update(Inventory inventoryFromDb, Inventory inventory) throws IOException {
        inventoryFromDb.setTitle(inventory.getTitle());
        inventoryFromDb.setDescription(inventory.getDescription());
        inventoryFromDb.setCategory(inventory.getCategory());
        inventoryFromDb.setDateModify(LocalDateTime.now());

        return inventoryRepository.save(inventoryFromDb);
    }

    public void delete(Inventory inventory) {
        inventoryRepository.delete(inventory);
    }

    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }

    public PageDto findAll(Pageable pageable) {
        Page<Inventory> page = inventoryRepository.findAll(pageable);

        return new PageDto(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }
}