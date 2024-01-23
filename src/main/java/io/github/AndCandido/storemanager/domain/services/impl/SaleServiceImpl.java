package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements ISaleService {

    private final ISaleRepository saleRepository;
    private final IProductService productService;
    private final IProductSoldService productSoldService;
    private final ICustomerService customerService;
    private final IInstallmentService installmentService;

    @Override
    @Transactional
    public Sale saveSale(SaleRequestDto saleRequestDto) {
        var sale = createSale(saleRequestDto);
        sale = saleRepository.save(sale);

        handlerCustomer(sale, saleRequestDto);
        handlerProductsSold(sale, saleRequestDto);
        handlerInstallments(sale, saleRequestDto);

        return sale;
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public Sale getSaleById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }

    @Override
    @Transactional
    public Sale updateSale(SaleRequestDto saleRequestDto, UUID id) {
        var sale = getSaleById(id);

        handlerCustomer(sale, saleRequestDto);

        handlerReturnStockQuantityForProducts(sale);

        handlerProductsSold(sale, saleRequestDto);
        handlerInstallments(sale, saleRequestDto);

        return saleRepository.save(sale);
    }

    @Override
    @Transactional
    public void deleteSale(UUID id) {
        var sale = getSaleById(id);

        for (ProductSold productSold : sale.getProductsSold()) {
            productService.returnStockQuantityByProductSold(productSold);
        }

        saleRepository.delete(sale);
    }

    private Sale createSale(SaleRequestDto saleResponseDto) {
        var sale = new Sale();
        BeanUtils.copyProperties(saleResponseDto, sale);
        return sale;
    }

    private void handlerCustomer(Sale sale, SaleRequestDto saleRequestDto) {
        UUID customerId = saleRequestDto.customerId();
        if(customerId == null) return;

        var customer = customerService.getCustomerById(customerId);

        if(customer != null) {
            sale.setCustomer(customer);
        }
    }

    private void handlerProductsSold(Sale sale, SaleRequestDto saleRequestDto) {
        for (ProductSoldRequestDto productSoldRequestDto : saleRequestDto.productsSold()) {
            var productSold = productSoldService.createProductSold(productSoldRequestDto);
            productSold.setSale(sale);
            sale.getProductsSold().add(productSold);
        }
    }

    private void handlerInstallments(Sale sale, SaleRequestDto saleRequestDto) {
        for (InstallmentRequestDto installmentRequestDto : saleRequestDto.installments()) {
            var installment = createInstallment(installmentRequestDto);
            installment.setCustomer(sale.getCustomer());
            installment.setSale(sale);

            sale.getInstallments().add(installment);
        }
    }

    private void handlerReturnStockQuantityForProducts(Sale sale) {
        for (ProductSold productSold : sale.getProductsSold()) {
            productService.returnStockQuantityByProductSold(productSold);
            productSoldService.deleteProductSold(productSold.getId());
        }
        sale.getProductsSold().clear();
    }

    private Installment createInstallment(InstallmentRequestDto installmentRequestDto) {
        return installmentService.createInstallment(installmentRequestDto);
    }
}
