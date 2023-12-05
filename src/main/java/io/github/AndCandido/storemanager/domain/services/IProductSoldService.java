package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.UUID;

public interface IProductSoldService {

    ProductSold createProductSold(ProductSoldDto productSoldDto);

    ProductSold getProductSoldById(UUID id);

    void deleteProductSold(UUID id);
}
