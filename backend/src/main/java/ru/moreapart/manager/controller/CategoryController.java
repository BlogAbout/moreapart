package ru.moreapart.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.moreapart.manager.entity.Category;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.service.CategoryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Category> list = categoryService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> info(@PathVariable("id") Category category) {
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Category category
    ) {
        try {
            Category updated  = categoryService.create(category);

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Category categoryFromDb,
            @RequestBody Category category
    ) {
        try {
            Category updated  = categoryService.update(categoryFromDb, category);

            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("error-item-update"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Category category) {
        categoryService.delete(category);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success-item-remove"));
    }
}