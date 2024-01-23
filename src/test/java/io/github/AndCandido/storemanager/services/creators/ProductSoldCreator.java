package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;

public class ProductSoldCreator {
    public static ProductSoldRequestDto createProductSoldRequestDto(Long productId, int quantity) {
        return ProductSoldRequestDto.builder()
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}
