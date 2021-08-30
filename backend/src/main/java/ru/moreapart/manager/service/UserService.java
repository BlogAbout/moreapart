package ru.moreapart.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.moreapart.manager.entity.Rights;
import ru.moreapart.manager.entity.Role;
import ru.moreapart.manager.entity.User;
import ru.moreapart.manager.repository.RoleRepository;
import ru.moreapart.manager.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) throws IOException {
        user.setRegistration(LocalDateTime.now());
        user.setActive(true);

        Set<Role> roles = new HashSet<>();
        /*Role userRole = (Role) roleRepository
                .findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));*/
        Role userRole = roleRepository.findById(1l).orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
        // Исправить ошибку

        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User update(User userFromDb, User user) throws IOException {
        userFromDb.setUsername(user.getUsername());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setAbout(user.getAbout());
        userFromDb.setEmail(user.getEmail());
        userFromDb.setPhone(user.getPhone());
        userFromDb.setBirthDate(user.getBirthDate());
        userFromDb.setRights(user.getRights());

        if (user.getPassword() != null && !user.getPassword().trim().isEmpty())
            userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));

        // Доделать: подтверждение мыла, если изменилось
        return userRepository.save(userFromDb);
    }

    public void delete(User user) {
        userRepository.delete(user);
        // Доделать: обновление количества пользователей по ролям
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public int countByRights(Rights rights) {
        return userRepository.countByRights(rights);
    }
}