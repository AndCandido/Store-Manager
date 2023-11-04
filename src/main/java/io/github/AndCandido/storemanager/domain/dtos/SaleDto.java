package io.github.AndCandido.storemanager.domain.dtos;

import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record SaleDto(
        @NotNull(message = "{sale.field.duplication.null}")
        @Min(value = 1, message = "{sale.field.duplication.min}")
        Short duplication,

        @NotNull(message = "{sale.field.paymentMethod.blank}")
        PaymentMethod paymentMethod,

        CustomerModel customer,

        @NotEmpty(message = "{sale.field.productsSold.empty}")
        @Valid List<ProductSoldDto> productsSold,

        @NotNull(message = "{sale.field.price.null}")
        @Min(value = 0, message = "{sale.field.price.min}")
        BigDecimal price
) {
}
