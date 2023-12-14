package io.github.AndCandido.storemanager.domain.annotations.enums;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;

public enum InstallmentDtoFieldsValidator {

    HAVE_PAYMENT_METHOD_WHEN_IS_PAID {
        @Override
        public boolean validate(InstallmentDto installmentDto) {
            return installmentDto.isPaid() == (installmentDto.paymentMethod() != null);
        }
    };

    public boolean validate(InstallmentDto installmentDto) {
        return false;
    }
}
