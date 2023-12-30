package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.UserDto;

public interface UserService {
    void saveUser(UserDto userDto);
}
