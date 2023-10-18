package io.github.AndCandido.storemanager.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductSold(
        @NotNull(message = "{productSold.field.id.null}")
        UUID id,

        @NotNull(message = "{productSold.field.quantity.null}")
        @Min(value = 1, message = "{productSold.field.quantity.min}")
        Integer quantity
) {
}
