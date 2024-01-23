package io.github.AndCandido.storemanager.domain.dtos.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record SaleResponseDto(

    UUID id,

    CustomerResponseDto customer,

    List<ProductSoldResponseDto> productsSold,

    List<InstallmentResponseDto> installments,

    Double price,

    LocalDateTime createdAt
) {
}
