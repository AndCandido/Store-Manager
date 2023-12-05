package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.*;
import io.github.AndCandido.storemanager.domain.validators.InstallmentValidator;
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
    public Sale saveSale(SaleDto saleDto) {
        var sale = createSale(saleDto);
        sale = saleRepository.save(sale);

        handlerCustomer(sale, saleDto);
        handlerProductsSold(sale, saleDto);
        handlerInstallments(sale, saleDto);


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
    public Sale updateSale(SaleDto saleDto, UUID id) {
        var sale = getSaleById(id);

        handlerCustomer(sale, saleDto);

        handlerReturnStockQuantityForProducts(sale);

        handlerProductsSold(sale, saleDto);
        handlerInstallments(sale, saleDto);

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

    private Sale createSale(SaleDto saleDto) {
        var sale = new Sale();
        BeanUtils.copyProperties(saleDto, sale);
        return sale;
    }

    private void handlerCustomer(Sale sale, SaleDto saleDto) {
        var customer = getCustomer(saleDto.customer());

        if(customer != null) {
            sale.setCustomer(customer);
        }
    }

    private void handlerProductsSold(Sale sale, SaleDto saleDto) {
        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var productSold = createProductSold(productSoldDto);
            productSold.setSale(sale);
            sale.getProductsSold().add(productSold);
        }
    }

    private void handlerInstallments(Sale sale, SaleDto saleDto) {
        double totalPrice = 0;
        for (InstallmentDto installmentDto : saleDto.installments()) {
            InstallmentValidator.validateInstallmentBySale(saleDto, installmentDto);

            var installment = createInstallment(installmentDto);
            installment.setCustomer(sale.getCustomer());
            installment.setSale(sale);

            sale.getInstallments().add(installment);

            totalPrice += installmentDto.price();
        }

        InstallmentValidator.validateInstallmentTotalPrice(totalPrice, saleDto.price());
    }

    private void handlerReturnStockQuantityForProducts(Sale sale) {
        for (ProductSold productSold : sale.getProductsSold()) {
            productService.returnStockQuantityByProductSold(productSold);
            productSoldService.deleteProductSold(productSold.getId());
        }
        sale.getProductsSold().clear();
    }

    private Customer getCustomer(CustomerDto customerDto) {
        return customerService.getCustomerById(customerDto.id());
    }

    private ProductSold createProductSold(ProductSoldDto productSoldDto) {
        return productSoldService.createProductSold(productSoldDto);
    }

    private Installment createInstallment(InstallmentDto installmentDto) {
        return installmentService.createInstallment(installmentDto);
    }
}
