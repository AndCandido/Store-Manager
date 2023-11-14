package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;

public class ProductSoldCreator {
    public static ProductSoldDto createDto(Long productId, int quantity) {
        return ProductSoldDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
