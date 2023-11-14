package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;

import java.util.List;
import java.util.UUID;

public interface IProductSoldService {
    ProductSoldModel getProductSoldById(UUID id);

    List<ProductSoldModel> getProductSoldByProduct(ProductModel productModel);

    void deleteProductSold(UUID id);
}
