package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.SaleModel;

import java.util.List;
import java.util.UUID;

public interface ISaleService {

    SaleModel saveSale(SaleDto saleDto);

    List<SaleModel> getAllSales();

    SaleModel getSaleById(UUID id);

    SaleModel updateSale(SaleDto saleDto, UUID id);

    void deleteSale(UUID id);
}
