package io.github.AndCandido.storemanager.domain.annotations.enums;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;

public enum SaleDtoFieldsValidator {
    INSTALLMENTS_PRICE_LESS_THAN_SALE_PRICE {
        @Override
        public boolean validate(SaleDto saleDto) {
            var installments = saleDto.installments();
            if(installments == null) return true;

            double totalPriceInstallments = 0;

            for (InstallmentDto installment : installments) {
                totalPriceInstallments += installment.price();
            }

            return totalPriceInstallments <= saleDto.price();
        }
    },

    NO_HAVE_CUSTOMER_MUST_BE_ONLY_ONE_INSTALLMENT_PAID {
        @Override
        public boolean validate(SaleDto saleDto) {
            if(saleDto.customer() != null)
                return true;

            boolean haveOnlyOneInstallment = saleDto.installments().size() == 1;
            boolean isPaid = saleDto.installments().get(0).isPaid();

            return haveOnlyOneInstallment && isPaid;
        }
    };

    public boolean validate(SaleDto saleDto) {
        return false;
    }
}
