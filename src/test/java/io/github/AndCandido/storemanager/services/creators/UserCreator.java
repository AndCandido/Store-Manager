package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.enums.Role;
import io.github.AndCandido.storemanager.domain.models.User;

public class UserCreator {
    public static User createModel(String username, String password, Role role) {
        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
