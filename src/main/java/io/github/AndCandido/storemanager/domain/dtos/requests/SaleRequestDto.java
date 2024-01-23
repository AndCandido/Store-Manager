package io.github.AndCandido.storemanager.domain.dtos.requests;

import io.github.AndCandido.storemanager.domain.annotations.ValidateSaleDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.SaleDtoFieldsValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

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
public record SaleRequestDto(

    UUID customerId,

    @NotEmpty(message = "{sale.field.productsSold.empty}")
    @Valid
    List<ProductSoldRequestDto> productsSold,

    @NotEmpty(message = "{sale.field.installments.empty}")
    @Valid
    List<InstallmentRequestDto> installments,

    @NotNull(message = "{sale.field.price.null}")
    @Min(value = 0, message = "{sale.field.price.min}")
    Double price
) {
}
