package ru.moreapart.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import ru.moreapart.manager.entity.Rights;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.service.EmailService;
import ru.moreapart.manager.service.RightsService;
import ru.moreapart.manager.service.UserService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserService userService;
    private final RightsService rightsService;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, RightsService rightsService, EmailService emailService) {
        this.userService = userService;
        this.rightsService = rightsService;
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<?> list() {
        List<User> list = userService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> info(@PathVariable("id") User user) {
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> data) {
        User user = new User(data);

        if (userService.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("username-is-exist"));

        if (userService.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("email-is-exist"));

        Rights rights = rightsService.findById(Long.parseLong(data.get("rights")));
        user.setRights(rights);

        try {
            User updated = userService.create(user);

            if (Boolean.parseBoolean(data.get("sendPassword")) && !user.getPassword().trim().isEmpty()) {
                try {
                    Context context = new Context();
                    context.setVariable("loginURL", "http://localhost:8081/login");
                    context.setVariable("userName", user.getFullName());
                    context.setVariable("login", user.getUsername());
                    context.setVariable("password", data.get("password"));
                    emailService.sendSimpleMessage(user.getEmail(), "Создан новый личный кабинет", "user-created", context);
                } catch (MessagingException e) {
                    //System.out.println(e.getMessage());
                }
            }

            rightsService.updateCountPersons(rights, userService.countByRights(rights));

            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("error-item-create"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") User userFromDb,
            @RequestBody Map<String, String> data
    ) {
        User user = new User(data);

        if (!userFromDb.getUsername().equals(user.getUsername()) && userService.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("username-is-exist"));

        if (!userFromDb.getEmail().equals(user.getEmail()) && userService.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("email-is-exist"));

        Rights rights = rightsService.findById(Long.parseLong(data.get("rights")));
        Rights oldRights = rightsService.findById(userFromDb.getRights().getId());
        user.setRights(rights);

        try {
            User updated = userService.update(userFromDb, user);

            if (Boolean.parseBoolean(data.get("sendPassword")) && !user.getPassword().trim().isEmpty()) {
                try {
                    Context context = new Context();
                    context.setVariable("loginURL", "http://localhost:8081/login");
                    context.setVariable("userName", user.getFullName());
                    context.setVariable("login", user.getUsername());
                    context.setVariable("password", data.get("password"));
                    emailService.sendSimpleMessage(user.getEmail(), "Обновлены данные для входа", "user-updated", context);
                } catch (MessagingException e) {
                    //System.out.println(e.getMessage());
                }
            }

            if (rights != oldRights) {
                rightsService.updateCountPersons(rights, userService.countByRights(rights));
                rightsService.updateCountPersons(oldRights, userService.countByRights(oldRights));
            }

            return ResponseEntity.status(HttpStatus.OK).body(updated);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(new MessageResponse("error-item-update"));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") User user) {
        userService.delete(user);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("success-item-remove"));
    }
}