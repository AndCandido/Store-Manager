package io.github.AndCandido.storemanager.domain.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserRequestDto(
    @NotBlank(message = "{user.field.username.blank}")
    String username,

    @NotBlank(message = "{user.field.password.blank}")
    String password
) {
}
