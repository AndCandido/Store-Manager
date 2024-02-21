package io.github.AndCandido.storemanager.domain.dtos.requests;

import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentInstallment(
    @NotNull(message = "{installment.field.paymentMethod.null}")
    PaymentMethod paymentMethod,

    @NotNull(message = "{installment.field.isPaid.null}")
    Boolean isPaid
) {
}
