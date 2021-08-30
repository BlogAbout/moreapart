package ru.moreapart.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.moreapart.manager.dto.ChecksAnswersPageDto;
import ru.moreapart.manager.dto.ChecksPageDto;
import ru.moreapart.manager.entity.Checks;
import ru.moreapart.manager.entity.ChecksAnswers;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.service.ChecksService;
import ru.moreapart.manager.service.UserDetailsImpl;

import java.io.IOException;

@RestController
@RequestMapping("/api/checks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChecksController {
    public static final int CHECKS_PER_PAGE = 15;

    private final ChecksService checksService;

    @Autowired
    public ChecksController(ChecksService checksService) {
        this.checksService = checksService;
    }

    @GetMapping
    public ResponseEntity<?> list(
            @PageableDefault(size = CHECKS_PER_PAGE, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        ChecksPageDto list = checksService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/reports")
    public ResponseEntity<?> reports(
            @PageableDefault(size = CHECKS_PER_PAGE, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable
    ) {
        ChecksAnswersPageDto list = checksService.findAllReports(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> info(@PathVariable("id") Checks checks) {
        return ResponseEntity.status(HttpStatus.OK).body(checks);
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<?> reportInfo(@PathVariable("id") ChecksAnswers checksAnswers) {
        checksAnswers.setChecksInfo(checksService.findById(checksAnswers.getCheck()));

        return ResponseEntity.status(HttpStatus.OK).body(checksAnswers);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Checks checks,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            Checks updated  = checksService.create(checks, userDetails.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @PostMapping("/fill")
    public ResponseEntity<?> fill(
            @RequestBody ChecksAnswers checksAnswers,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        try {
            ChecksAnswers updated = checksService.fill(checksAnswers, userDetails.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-fill"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Checks checksFromDb,
            @RequestBody Checks checks
    ) {
        try {
            Checks updated  = checksService.update(checksFromDb, checks);

            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("error-item-update"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Checks checks) {
        checksService.delete(checks);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success-item-remove"));
    }
}