package io.github.AndCandido.storemanager.api.exceptions;

public class InvalidPaymentMethodException extends IllegalArgumentException {

    public InvalidPaymentMethodException() {
        super();
    }

    public InvalidPaymentMethodException(String message) {
        super(message);
    }
}
