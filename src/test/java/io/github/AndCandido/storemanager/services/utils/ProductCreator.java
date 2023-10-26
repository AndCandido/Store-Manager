package io.github.AndCandido.storemanager.services.utils;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;

import java.math.BigDecimal;

public class ProductCreator {
    public static ProductDto createProductDto(String name, double price, int stockQuantity) {
        return ProductDto.builder()
                .name(name)
                .price(BigDecimal.valueOf(price).setScale(2))
                .stockQuantity(stockQuantity)
                .build();
    }

    public static ProductModel createProductModel(String name, double price, int stockQuantity) {
        return ProductModel.builder()
                .name(name)
                .price(BigDecimal.valueOf(price).setScale(2))
                .stockQuantity(stockQuantity)
                .build();
    }
}
