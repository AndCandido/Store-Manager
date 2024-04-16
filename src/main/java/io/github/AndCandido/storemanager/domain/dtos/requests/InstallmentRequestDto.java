package io.github.AndCandido.storemanager.domain.dtos.requests;

import io.github.AndCandido.storemanager.domain.annotations.ValidateInstallmentDto;
import io.github.AndCandido.storemanager.domain.annotations.enums.InstallmentDtoFieldsValidator;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@ValidateInstallmentDto.List({
    @ValidateInstallmentDto(
        message = "{validation.installment.havePaymentMethodWhenIsPaid}",
        fieldsValidator = InstallmentDtoFieldsValidator.HAVE_PAYMENT_METHOD_WHEN_IS_PAID
    ),
    @ValidateInstallmentDto(
        message = "{installment.field.dueDate.onlyHaveDueDateWhenNotPaid}",
        fieldsValidator = InstallmentDtoFieldsValidator.ONLY_HAVE_DUE_DATE_WHEN_NOT_PAID
    )
})
public record InstallmentRequestDto(

    @Future(message = "{installment.field.dueDate.future}")
    LocalDate dueDate,

    @NotNull(message = "{installment.field.price.null}")
    @Min(value = 0, message = "{installment.field.price.min}")
    Double price,

    PaymentMethod paymentMethod,

    @NotNull(message = "{installment.field.isPaid.null}")
    Boolean isPaid,

    @Valid
    CustomerRequestDto customer,

    @Valid
    SaleRequestDto sale
) {
}
