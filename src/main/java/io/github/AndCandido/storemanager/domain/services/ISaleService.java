package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;
import java.util.UUID;

public interface ISaleService {

    Sale saveSale(SaleRequestDto saleRequestDto);

    List<Sale> getAllSales();

    Sale getSaleById(UUID id);

    Sale updateSale(SaleRequestDto saleRequestDto, UUID id);

    void deleteSale(UUID id);
}
