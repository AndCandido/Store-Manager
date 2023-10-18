package io.github.AndCandido.storemanager.api.exceptions;

public class InsufficientStockException extends IllegalArgumentException {

    public InsufficientStockException() {
        super();
    }

    public InsufficientStockException(String message) {
        super(message);
    }
}
