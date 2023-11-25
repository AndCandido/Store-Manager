package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;
import java.util.UUID;

public interface IProductSoldService {

    List<ProductSold> saveProductsSoldBySale(Sale sale, SaleDto saleDto);

    ProductSold getProductSoldById(UUID id);

    void deleteProductSold(UUID id);
}
