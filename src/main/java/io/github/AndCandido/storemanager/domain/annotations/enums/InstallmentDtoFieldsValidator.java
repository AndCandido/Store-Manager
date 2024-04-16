package io.github.AndCandido.storemanager.domain.annotations.enums;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;

public enum InstallmentDtoFieldsValidator {

    ONLY_HAVE_DUE_DATE_WHEN_NOT_PAID {
        @Override
        public boolean validate(InstallmentRequestDto installmentResponseDto) {
            return installmentResponseDto.isPaid() == (installmentResponseDto.dueDate() == null);
        }
    },

    HAVE_PAYMENT_METHOD_WHEN_IS_PAID {
        @Override
        public boolean validate(InstallmentRequestDto installmentResponseDto) {
            if(installmentResponseDto == null) return true;
            return installmentResponseDto.isPaid() == (installmentResponseDto.paymentMethod() != null);
        }
    };

    public abstract boolean validate(InstallmentRequestDto installmentResponseDto);
}
