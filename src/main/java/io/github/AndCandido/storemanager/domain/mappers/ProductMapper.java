package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.Product;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if(product == null) return null;

        var productsSoldDto = ProductSoldMapper.toDtoList(product.getProductsSold());

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .productsSold(productsSoldDto)
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }

}
