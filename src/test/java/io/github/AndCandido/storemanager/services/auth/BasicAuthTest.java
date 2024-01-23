package io.github.AndCandido.storemanager.services.auth;

import io.github.AndCandido.storemanager.domain.enums.Role;
import io.github.AndCandido.storemanager.domain.models.User;
import io.github.AndCandido.storemanager.domain.repositories.IUserRepository;
import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthTest {

    private final IUserRepository userRepository;

    @Getter
    private String username;
    @Getter
    private String password;

    private User user;

    public BasicAuthTest(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.username = "admin";
        this.password = "12345";
        createUser();
        saveUser();
    }

    private void createUser() {
        this.user = new User(
            null,
            this.username,
            new BCryptPasswordEncoder().encode(this.password),
            Role.ADMIN
        );
    }

    private void saveUser() {
        userRepository.save(user);
    }
}
