package io.github.AndCandido.storemanager.domain.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductSoldDto(
        UUID id,

        @NotNull(message = "{productSold.field.id.null}")
        Long productId,

        @NotNull(message = "{productSold.field.quantity.null}")
        @Min(value = 1, message = "{productSold.field.quantity.min}")
        Integer quantity
) {
}
