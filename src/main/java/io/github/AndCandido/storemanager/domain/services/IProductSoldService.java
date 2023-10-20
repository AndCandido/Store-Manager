package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;

import java.util.List;
import java.util.UUID;

public interface IProductSoldService {
    ProductSoldModel saveProductSold(ProductSoldDto productSoldDto);

    List<ProductSoldModel> getProductsSold();

    ProductSoldModel getProductSoldById(UUID id);

    ProductSoldModel updateProductSold(ProductSoldDto productSoldDto, UUID id);

    void deleteProductSold(UUID id);
}
