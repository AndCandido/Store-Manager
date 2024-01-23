package io.github.AndCandido.storemanager.domain.annotations.enums;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;

public enum SaleDtoFieldsValidator {
    INSTALLMENTS_PRICE_LESS_THAN_SALE_PRICE {
        @Override
        public boolean validate(SaleRequestDto saleRequestDto) {
            var installments = saleRequestDto.installments();
            if(installments == null) return true;

            double totalPriceInstallments = 0;

            for (InstallmentRequestDto installment : installments) {
                totalPriceInstallments += installment.price();
            }

            return totalPriceInstallments <= saleRequestDto.price();
        }
    },

    NO_HAVE_CUSTOMER_MUST_BE_ONLY_ONE_INSTALLMENT_PAID {
        @Override
        public boolean validate(SaleRequestDto saleRequestDto) {
            if(saleRequestDto.customerId() != null)
                return true;

            boolean haveOnlyOneInstallment = saleRequestDto.installments().size() == 1;
            boolean isPaid = saleRequestDto.installments().get(0).isPaid();

            return haveOnlyOneInstallment && isPaid;
        }
    };

    public abstract boolean validate(SaleRequestDto saleRequestDto);
}
