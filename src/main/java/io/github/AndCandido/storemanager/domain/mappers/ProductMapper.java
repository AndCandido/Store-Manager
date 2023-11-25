package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.Product;
import org.springframework.beans.BeanUtils;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if(product == null) return null;

        var productsSoldDto = ProductSoldMapper.toDtoList(product.getProductsSold());

        return new ProductDto(
                product.getId(),
                product.getName(),
                productsSoldDto,
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt()
        );
    }

}
