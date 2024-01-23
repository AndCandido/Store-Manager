package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;

public class ProductCreator {
    public static ProductRequestDto createProductRequestDto(String name, double price, int stockQuantity) {
        return ProductRequestDto.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}
