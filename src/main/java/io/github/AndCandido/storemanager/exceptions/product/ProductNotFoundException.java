package io.github.AndCandido.storemanager.exceptions.product;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException() {
        super();
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
