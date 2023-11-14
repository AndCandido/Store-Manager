package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import org.springframework.beans.BeanUtils;

public class ProductMapper {

    public static ProductModel toModel(ProductDto productDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);
        return productModel;
    }

    public static ProductDto toDto(ProductModel productModel) {
        var productSoldModels = productModel.getProductsSold();
        var productSoldDtos = productSoldModels == null || productSoldModels.isEmpty() ? null
            : ProductSoldMapper.toDtoList(productModel.getProductsSold());

        return new ProductDto(
                productModel.getId(),
                productModel.getName(),
                productSoldDtos,
                productModel.getPrice(),
                productModel.getStockQuantity(),
                productModel.getCreatedAt()
        );
    }

}
