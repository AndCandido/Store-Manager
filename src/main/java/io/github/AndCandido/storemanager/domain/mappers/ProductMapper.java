package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;
import io.github.AndCandido.storemanager.domain.models.Product;

public class ProductMapper {

    public static ProductResponseDto toDto(Product product) {
        if(product == null) return null;

        var productsSoldDto = ProductSoldMapper.toDtoList(product.getProductsSold());

        return ProductResponseDto.builder()
                .id(product.getId())
                .ref(product.getRef())
                .name(product.getName())
                .productsSold(productsSoldDto)
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }

}
