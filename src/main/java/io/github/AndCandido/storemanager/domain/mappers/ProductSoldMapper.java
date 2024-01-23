package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductSoldResponseDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.List;

public class ProductSoldMapper {

    public static ProductSoldRequestDto toRequestDto(ProductSold productSold) {
        if(productSold == null) return null;

        return ProductSoldRequestDto.builder()
            .productId(productSold.getProduct().getId())
            .quantity(productSold.getQuantity())
            .build();
    }

    public static ProductSoldResponseDto toDto(ProductSold productSold) {
        if(productSold == null) return null;
        var product = productSold.getProduct();

        return ProductSoldResponseDto.builder()
                .id(productSold.getId())
                .productId(product == null ? null : product.getId())
                .quantity(productSold.getQuantity())
                .build();
    }

    public static List<ProductSoldResponseDto> toDtoList(List<ProductSold> productsSold) {
        return productsSold == null || productsSold.isEmpty() ? null
            : productsSold.stream().map(ProductSoldMapper::toDto).toList();
    }
}