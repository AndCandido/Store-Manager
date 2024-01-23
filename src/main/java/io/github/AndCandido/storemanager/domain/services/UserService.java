package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.UserRequestDto;

public interface UserService {
    void saveUser(UserRequestDto userRequestDto);
}
