package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.IllegalClientActionException;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleServiceImpl implements ISaleService {

    private ISaleRepository saleRepository;
    private IProductService productService;
    private IProductSoldService productSoldService;
    private ICustomerService customerService;
    private IInstallmentService installmentService;

    public SaleServiceImpl(ISaleRepository saleRepository, IProductService productService, IProductSoldService productSoldService, ICustomerService customerService, IInstallmentService installmentService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.productSoldService = productSoldService;
        this.customerService = customerService;
        this.installmentService = installmentService;
    }

    @Override
    @Transactional
    public Sale saveSale(SaleDto saleDto) {
        var sale = createSale(saleDto);
        sale = saleRepository.save(sale);

        setCustomerOnSale(sale, saleDto);

        checkIfInstallmentsIsValid(sale, saleDto);

        setProductSoldOnSale(sale, saleDto);
        setInstallmentsOnSale(sale, saleDto);

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

        setCustomerOnSale(sale, saleDto);

        checkIfInstallmentsIsValid(sale, saleDto);

        for (ProductSold productSold : sale.getProductsSold()) {
            productService.returnStockQuantityByProductSold(productSold);
            productSoldService.deleteProductSold(productSold.getId());
        }

        sale.getProductsSold().clear();
        setProductSoldOnSale(sale, saleDto);

        return saleRepository.save(sale);
    }

    @Override
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

    private void setCustomerOnSale(Sale sale, SaleDto saleDto) {
        if(saleDto.customer() != null) {
            var customer = customerService.getCustomerById(saleDto.customer().id());
            sale.setCustomer(customer);
        }
    }

    private void checkIfInstallmentsIsValid(Sale sale, SaleDto saleDto) {
        for (InstallmentDto installmentDto : saleDto.installments()) {
            if(installmentDto.isPaid() && installmentDto.paymentMethod() == null) {
                throw new IllegalClientActionException("Deve se informado a forma de pagamento quando a parcela é paga");
            }

            if(sale.getCustomer() == null && !installmentDto.isPaid()) {
                throw new IllegalClientActionException("Não pode haver parcelas não pagas caso a venda não tenha um cliente declarado");
            }
        }

    }

    private void setProductSoldOnSale(Sale sale, SaleDto saleDto) {
        var productsSoldSaved = productSoldService.saveProductsSoldBySale(sale, saleDto);
        sale.setProductsSold(productsSoldSaved);
    }

    private void setInstallmentsOnSale(Sale sale, SaleDto saleDto) {
        var installmentsSaved = installmentService.saveInstallmentBySale(sale, saleDto);
        sale.setInstallments(installmentsSaved);
    }
}
