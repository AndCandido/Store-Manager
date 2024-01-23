package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductSoldResponseDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.UUID;

public interface IProductSoldService {

    ProductSold createProductSold(ProductSoldRequestDto productSoldRequestDto);

    ProductSold getProductSoldById(UUID id);

    void deleteProductSold(UUID id);
}
