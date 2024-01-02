package io.github.AndCandido.storemanager.domain.dtos;

import io.github.AndCandido.storemanager.domain.dtos.groups.RequestGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductSoldDto(

    @Null(message = "{field.id.null}", groups = RequestGroup.class)
    UUID id,

    @NotNull(message = "{productSold.field.id.null}", groups = RequestGroup.class)
    Long productId,

    @NotNull(message = "{productSold.field.quantity.null}", groups = RequestGroup.class)
    @Min(value = 1, message = "{productSold.field.quantity.min}", groups = RequestGroup.class)
    Integer quantity
) {
}
