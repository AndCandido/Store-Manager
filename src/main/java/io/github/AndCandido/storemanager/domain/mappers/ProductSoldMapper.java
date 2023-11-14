package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;

import java.util.List;

public class ProductSoldMapper {

    public static ProductSoldDto toDto(ProductSoldModel productSoldModel) {
        var productModel = productSoldModel.getProductModel();
        return new ProductSoldDto(
                productModel == null ? null : productModel.getId(),
                productSoldModel.getQuantity()
        );
    }

    public static List<ProductSoldDto> toDtoList(List<ProductSoldModel> productSoldModels) {
        return productSoldModels.stream().map(ProductSoldMapper::toDto).toList();
    }
}