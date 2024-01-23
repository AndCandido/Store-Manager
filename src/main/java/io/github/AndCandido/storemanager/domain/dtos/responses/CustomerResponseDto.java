package io.github.AndCandido.storemanager.domain.dtos.responses;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record CustomerResponseDto(

    UUID id,

    String name,

    String nickname,

    String cpf,

    String address,

    String phone,

    List<SaleResponseDto> sales,

    List<InstallmentResponseDto> installments,

    LocalDateTime createdAt
) {
}
