package io.github.AndCandido.storemanager.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record SaleDto(
        UUID id,

        @NotNull(message = "{sale.field.duplication.null}")
        @Min(value = 1, message = "{sale.field.duplication.min}")
        Short duplication,

        @NotNull(message = "{sale.field.paymentMethod.blank}")
        PaymentMethod paymentMethod,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        CustomerDto customer,

        @NotEmpty(message = "{sale.field.productsSold.empty}")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Valid List<ProductSoldDto> productsSold,

        @NotNull(message = "{sale.field.price.null}")
        @Min(value = 0, message = "{sale.field.price.min}")
        BigDecimal price,

        LocalDateTime createdAt
) {
}
