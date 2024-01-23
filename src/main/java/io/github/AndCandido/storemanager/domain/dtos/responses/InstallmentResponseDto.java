package io.github.AndCandido.storemanager.domain.dtos.responses;

import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InstallmentResponseDto(

    UUID id,

    LocalDate dueDate,

    Double price,

    PaymentMethod paymentMethod,

    Boolean isPaid,

    CustomerResponseDto customer,

    SaleResponseDto sale,

    LocalDateTime createdAt
) {
}
