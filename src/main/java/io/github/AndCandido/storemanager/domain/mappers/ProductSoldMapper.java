package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.List;

public class ProductSoldMapper {

    public static ProductSoldDto toDto(ProductSold productSold) {
        var product = productSold.getProduct();

        return productSold == null ? null
            : new ProductSoldDto(
                productSold.getId(),
                product == null ? null : product.getId(),
                productSold.getQuantity()
        );
    }

    public static List<ProductSoldDto> toDtoList(List<ProductSold> productsSold) {
        return productsSold == null || productsSold.isEmpty() ? null
            : productsSold.stream().map(ProductSoldMapper::toDto).toList();
    }
}