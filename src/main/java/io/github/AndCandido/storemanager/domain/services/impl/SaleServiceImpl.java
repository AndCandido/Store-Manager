package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.InsufficientStockException;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import io.github.AndCandido.storemanager.domain.services.util.SaleUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

@Service
public class SaleServiceImpl implements ISaleService {

    private ISaleRepository saleRepository;
    private SaleUtil saleUtil;

    public SaleServiceImpl(ISaleRepository saleRepository, SaleUtil saleUtil) {
        this.saleRepository = saleRepository;
        this.saleUtil = saleUtil;
    }

    @Override
    public SaleModel saveSale(SaleDto saleDto) {
        SaleModel saleModel = new SaleModel();
        saleUtil.handlerSetSaleModel(saleDto, saleModel);
        return saleRepository.save(saleModel);
    }

    @Override
    public List<SaleModel> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public SaleModel updateSale(SaleDto saleDto, UUID id) {
        var saleModel = getSaleById(id);
        return saleRepository.save(saleModel);
    }

    @Override
    public void deleteSale(UUID id) {
        var sale = getSaleById(id);
        saleRepository.delete(sale);
    }

    @Override
    public SaleModel getSaleById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }
}