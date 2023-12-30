package io.github.AndCandido.storemanager.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDto(
    @NotBlank(message = "{user.field.username.blank}")
    String username,

    @NotBlank(message = "{user.field.password.blank}")
    String password
) {
}
