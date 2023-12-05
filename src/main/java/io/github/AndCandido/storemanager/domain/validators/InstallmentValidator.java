package io.github.AndCandido.storemanager.domain.validators;

import io.github.AndCandido.storemanager.api.exceptions.IllegalClientActionException;
import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.enums.PaymentMethod;

public final class InstallmentValidator {

    private InstallmentValidator() {
    }

    public static void validateInstallmentBySale(SaleDto saleDto, InstallmentDto installmentDto) {
        validatePaymentMethodForPaid(
                installmentDto.isPaid(), installmentDto.paymentMethod()
        );
        validateCustomerForUnPaid(
                installmentDto.isPaid(), saleDto.customer()
        );
    }

    public static void validateInstallmentTotalPrice(double totalPrice, double salePrice) {
        validateInstallmentsPriceForSalePrice(totalPrice, salePrice);
    }

    private static void validatePaymentMethodForPaid(boolean isPaid, PaymentMethod paymentMethod) {
        if (isPaid && paymentMethod == null) {
            throwIllegalClientActionException(
                    "Deve se informado a forma de pagamento quando a parcela é paga"
            );
        }
    }

    private static void validateCustomerForUnPaid(boolean isPaid, CustomerDto customer) {
        if (!isPaid && customer == null) {
            throwIllegalClientActionException(
                    "Não pode haver parcelas não pagas caso a venda não tenha um cliente declarado"
            );
        }
    }

    private static void validateInstallmentsPriceForSalePrice(double installmentsPrice, double salePrice) {
        if (installmentsPrice > salePrice) {
            throwIllegalClientActionException(
                    "O preço total das parcelas deve ser menor que o preço da venda"
            );
        }
    }

    private static void throwIllegalClientActionException(String message) {
        throw new IllegalClientActionException(message);
    }
}

