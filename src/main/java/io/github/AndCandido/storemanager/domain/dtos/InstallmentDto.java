package io.github.AndCandido.storemanager.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InstallmentDto(
    UUID id,

    @NotNull(message = "{installment.field.dueDate.null}")
    @Future(message = "{installment.field.dueDate.future}")
    LocalDate dueDate,

    @NotNull(message = "{installment.field.price.null}")
    @Min(value = 0, message = "{installment.field.price.min}")
    BigDecimal price,

    PaymentMethod paymentMethod,

    @NotNull(message = "{installment.field.isPaid.null}")
    boolean isPaid,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    CustomerDto customer,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Valid
    SaleDto sale,

    LocalDateTime createdAt
) {
}
