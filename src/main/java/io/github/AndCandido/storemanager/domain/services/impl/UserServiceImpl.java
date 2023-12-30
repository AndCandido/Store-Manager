package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.AuthenticationClientException;
import io.github.AndCandido.storemanager.domain.dtos.UserDto;
import io.github.AndCandido.storemanager.domain.enums.Role;
import io.github.AndCandido.storemanager.domain.models.User;
import io.github.AndCandido.storemanager.domain.repositories.IUserRepository;
import io.github.AndCandido.storemanager.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final IUserRepository IUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(UserDto userDto) {
        if(isUserExisting(userDto.username())) {
            throw new AuthenticationClientException("Client already exists");
        }

        var passwordEncoded = passwordEncoder.encode(userDto.password());

        var user = new User();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoded);
        user.setRole(Role.USER);

        IUserRepository.save(user);
    }

    private boolean isUserExisting(String username) {
        User existingUser = IUserRepository.findByUsername(username);
        return existingUser != null;
    }
}
