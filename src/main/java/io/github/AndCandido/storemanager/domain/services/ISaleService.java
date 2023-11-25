package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;
import java.util.UUID;

public interface ISaleService {

    Sale saveSale(SaleDto saleDto);

    List<Sale> getAllSales();

    Sale getSaleById(UUID id);

    Sale updateSale(SaleDto saleDto, UUID id);

    void deleteSale(UUID id);
}
