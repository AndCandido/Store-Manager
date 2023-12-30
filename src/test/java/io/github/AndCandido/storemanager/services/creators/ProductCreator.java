package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;

public class ProductCreator {
    public static ProductDto createProductDto(String name, double price, int stockQuantity) {
        return ProductDto.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }
}
