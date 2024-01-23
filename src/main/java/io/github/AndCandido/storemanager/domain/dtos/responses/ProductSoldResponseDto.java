package io.github.AndCandido.storemanager.domain.dtos.responses;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductSoldResponseDto(

    UUID id,

    Long productId,

    Integer quantity
) {
}
