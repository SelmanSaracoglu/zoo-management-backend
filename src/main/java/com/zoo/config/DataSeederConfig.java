package com.zoo.config;

import com.zoo.user.Role;
import com.zoo.user.UserEntity;
import com.zoo.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.logging.Logger;

@Configuration
public class DataSeederConfig {

    private static final Logger log = Logger.getLogger(DataSeederConfig.class.getName());

    @Bean
    CommandLineRunner seedAdmin(UserRepository users,
                                PasswordEncoder encoder,
                                @Value("${app.admin.username:admin@zoo.io}") String username,
                                @Value("${app.admin.password:Admin123!}") String rawPassword) {
        return args -> {
            if (!users.existsByUsername(username)) {          // yoksa oluştur
                UserEntity u = new UserEntity();
                u.setUsername(username);
                u.setPassword(encoder.encode(rawPassword));     // BCrypt
                u.setRoles(Set.of(Role.ADMIN));
                users.save(u);
                log.info(() -> "Seeded ADMIN user: " + username);
            } else {
                log.info(() -> "ADMIN user already exists: " + username);
            }
        };
    }
}