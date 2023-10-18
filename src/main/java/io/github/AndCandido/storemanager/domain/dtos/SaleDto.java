package io.github.AndCandido.storemanager.domain.dtos;

import io.github.AndCandido.storemanager.api.request.ProductSold;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record SaleDto(

        @NotNull(message = "{sale.field.duplication.null}")
        @Min(value = 1, message = "{sale.field.duplication.min}")
        Short duplication,

        @NotNull(message = "{sale.field.paymentMethod.blank}")
        PaymentMethod paymentMethod,

        UUID clientId,

        @NotEmpty(message = "{sale.field.productsSold.empty}")
        List<@Valid ProductSold> productsSold
) {
}
