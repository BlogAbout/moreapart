package ru.moreapart.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.moreapart.manager.entity.Rights;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.service.RightsService;
import ru.moreapart.manager.service.UserDetailsImpl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RightsController {
    private final RightsService rightsService;

    @Autowired
    public RightsController(RightsService rightsService) {
        this.rightsService = rightsService;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<Rights> rights = rightsService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(rights);
    }

    @GetMapping("{id}")
    public Rights info(@PathVariable("id") Rights roleRights) {
        return roleRights;
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody Map<String, String> data,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Rights rights = new Rights(data);
        try {
            Rights updated = rightsService.create(rights, userDetails.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") Rights rightsFromDb,
            @RequestBody Map<String, String> data
    ) {
        Rights rights = new Rights(data);
        try {
            Rights updated = rightsService.update(rightsFromDb, rights);

            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("error-item-update"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Rights rights) {
        rightsService.delete(rights);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success-item-remove"));
    }
}