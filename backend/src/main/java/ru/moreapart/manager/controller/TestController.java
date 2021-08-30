package ru.moreapart.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.repository.UserRepository;
import ru.moreapart.manager.service.EmailService;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {
    @Autowired
    public EmailService emailService;

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/test-repo")
    public ResponseEntity<?> getRepo() {
        User user = userRepository.getById(1l);
        return ResponseEntity.status(HttpStatus.OK).body("hren");
    }

    @GetMapping("/all")
    public String getAll() {
        return "public API";
    }

    @GetMapping("/send-mail")
    public String sendMail() {
        try {
            Context context = new Context();
            context.setVariable("userName", "usernamePavel");
            context.setVariable("verificationURL", "http://localhost/test/");
            emailService.sendSimpleMessage("ozz.zol@yandex.ru", "Тестовое сообщение", "registration-user", context);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return "mailSend";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getUserApi() {
        return "User API";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String getModApi() {
        return "Moderator API";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminApi() {
        return "Admin API";
    }
}