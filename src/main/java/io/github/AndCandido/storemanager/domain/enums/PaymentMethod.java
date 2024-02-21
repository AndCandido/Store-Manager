package io.github.AndCandido.storemanager.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.AndCandido.storemanager.api.exceptions.InvalidPaymentMethodException;

public enum PaymentMethod {
    MONEY("money"),
    DEBIT_CARD("debit card"),
    CREDIT_CARD("credit card"),
    PIX("pix"),
    NEGOTIATED("negotiated");

    private String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentMethod fromString(String value) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value.equalsIgnoreCase(value)) {
                return paymentMethod;
            }
        }
        throw new InvalidPaymentMethodException("Método de pagamento inválido");
    }
}
