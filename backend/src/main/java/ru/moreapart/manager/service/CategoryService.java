package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moreapart.manager.entity.Category;
import ru.moreapart.manager.repository.CategoryRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) throws IOException {
        category.setCountInventory(0);
        category.setDateCreated(LocalDateTime.now());
        category.setDateModify(LocalDateTime.now());

        return categoryRepository.save(category);
    }

    public Category update(Category categoryFromDb, Category category) throws IOException {
        categoryFromDb.setTitle(category.getTitle());
        categoryFromDb.setDescription(category.getDescription());
        categoryFromDb.setDateModify(LocalDateTime.now());

        return categoryRepository.save(categoryFromDb);
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    private int countingInventory() {
        //Доделать: Подсчет числа оборудования в категории
        return 0;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}