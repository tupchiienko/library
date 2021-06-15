package com.cursor.library;

import com.cursor.library.entity.User;
import com.cursor.library.entity.UserPermission;
import com.cursor.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class LibraryApplication {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @PostConstruct
    public void addUsers() {
        var user = new User();
        user.setUsername("Vlad");
        user.setPassword(encoder.encode("1234"));
        user.setPermissions(Set.of(UserPermission.ROLE_USER));
        userRepository.save(user);
        var admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin"));
        admin.setPermissions(Set.of(UserPermission.ROLE_ADMIN));
        userRepository.save(admin);
    }
}
