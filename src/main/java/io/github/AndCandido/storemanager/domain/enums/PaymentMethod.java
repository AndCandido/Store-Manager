package io.github.AndCandido.storemanager.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    MONEY("money"),
    DEBIT_CARD("debit_card"),
    CREDIT_CARD("credit_card"),
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
            if (paymentMethod.name().equalsIgnoreCase(value)) {
                return paymentMethod;
            }
        }
        throw new IllegalArgumentException("Método de pagamento inválido");
    }
}
