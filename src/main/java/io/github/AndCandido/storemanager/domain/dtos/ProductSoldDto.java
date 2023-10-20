package io.github.AndCandido.storemanager.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public record ProductSoldDto(
        @NotNull(message = "{productSold.field.id.null}")
        Long productId,

        @NotNull(message = "{productSold.field.quantity.null}")
        @Min(value = 1, message = "{productSold.field.quantity.min}")
        Integer quantity
) {
}
