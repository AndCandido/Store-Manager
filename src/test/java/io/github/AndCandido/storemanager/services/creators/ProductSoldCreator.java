package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;

public class ProductSoldCreator {
    public static ProductSoldDto createProductSoldDto(Long productId, int quantity) {
        return ProductSoldDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
