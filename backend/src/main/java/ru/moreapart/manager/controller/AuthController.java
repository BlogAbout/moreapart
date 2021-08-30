package ru.moreapart.manager.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import jdk.vm.ci.meta.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import ru.moreapart.manager.config.jwt.JwtUtils;
import ru.moreapart.manager.entity.ERole;
import ru.moreapart.manager.entity.Role;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.pojo.JwtResponse;
import ru.moreapart.manager.pojo.LoginRequest;
import ru.moreapart.manager.pojo.MessageResponse;
import ru.moreapart.manager.pojo.SignupRequest;
import ru.moreapart.manager.repository.RoleRepository;
import ru.moreapart.manager.repository.UserRepository;
import ru.moreapart.manager.service.EmailService;
import ru.moreapart.manager.service.UserDetailsImpl;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("username-is-exist"));

        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("email-is-exist"));

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        user.setFirstName(signupRequest.getName());
        user.setPhone(signupRequest.getPhone());
        user.setActive(true);
        user.setRegistration(LocalDateTime.now());
        user.setLastVisit(LocalDateTime.now());

        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                switch (r) {
                    case "admin":
                        Role adminRole = roleRepository
                                .findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository
                                .findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error, Role MODERATOR is not found"));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository
                                .findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        // !!!Создать отдельный поток
        try {
            Context context = new Context();
            context.setVariable("userName", user.getFirstName());
            context.setVariable("verificationURL", "http://localhost/verification");
            emailService.sendSimpleMessage(user.getEmail(), "Завершение регистрации в сервисе Море Апартаментов", "registration-user", context);
        } catch (MessagingException e) {
            //System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("user-created"));
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> map) {
        String userEmail = map.get("email");

        if (userEmail == null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("notfound-data"));

        if (!userRepository.existsByEmail(userEmail))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("email-not-exist"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + userEmail));

        String uuid = String.valueOf(UUID.randomUUID());
        String key = new String(Base64.getEncoder().encode(uuid.getBytes()));
        user.setResetKey(key);
        userRepository.save(user);

        // !!!Создать отдельный поток
        try {
            Context context = new Context();
            context.setVariable("userName", user.getFirstName());
            context.setVariable("forgotUrl", "http://localhost:8081/reset?key=" + key);
            emailService.sendSimpleMessage(user.getEmail(), "Восстановление пароля в сервисе Море Апартаментов", "forgot-password", context);
        } catch (MessagingException e) {
            //System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(new MessageResponse("forgot-send"));
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> map) {
        String key = map.get("reset_key");
        String password = map.get("password");

        if (key == null)
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("notfound-data"));

        if (!userRepository.existsByResetKey(key))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("not-query-reset-key"));

        User user = userRepository.findByResetKey(key)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found."));

        user.setPassword(passwordEncoder.encode(password));
        user.setResetKey("");
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("reset-password"));
    }
}