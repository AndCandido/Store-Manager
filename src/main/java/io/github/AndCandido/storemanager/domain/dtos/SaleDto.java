package io.github.AndCandido.storemanager.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.AndCandido.storemanager.domain.annotations.ValidateSaleDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.SaleDtoFieldsValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@ValidateSaleDto.List({
    @ValidateSaleDto(
        message = "{validation.sale.installmentPricesLessThanSalePrice}",
        fieldsValidator = SaleDtoFieldsValidator.INSTALLMENTS_PRICE_LESS_THAN_SALE_PRICE
    ),
    @ValidateSaleDto(
        message = "{validation.sale.NoHaveCustomerMostBeOnlyInstallmentPaid}",
        fieldsValidator = SaleDtoFieldsValidator.NO_HAVE_CUSTOMER_MUST_BE_ONLY_ONE_INSTALLMENT_PAID
    ),
})
public record SaleDto(
        UUID id,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        CustomerDto customer,

        @NotEmpty(message = "{sale.field.productsSold.empty}")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Valid
        List<ProductSoldDto> productsSold,

        @NotEmpty(message = "{sale.field.installments.empty}")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @Valid
        List<InstallmentDto> installments,

        @NotNull(message = "{sale.field.price.null}")
        @Min(value = 0, message = "{sale.field.price.min}")
        Double price,

        LocalDateTime createdAt
) {
}
