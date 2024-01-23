package io.github.AndCandido.storemanager.domain.dtos.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductResponseDto(

    Long id,

    String name,

    List<ProductSoldResponseDto> productsSold,

    Double price,

    Integer stockQuantity,

    LocalDateTime createdAt
) {
}
