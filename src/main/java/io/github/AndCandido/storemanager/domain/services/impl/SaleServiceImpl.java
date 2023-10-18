package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.InsufficientStockException;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.api.request.ProductSold;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import io.github.AndCandido.storemanager.utils.ApplicationUtils;
import jakarta.validation.constraints.Min;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SaleServiceImpl implements ISaleService {

    private ISaleRepository saleRepository;
    private ICustomerService customerService;
    private IProductService productService;

    public SaleServiceImpl(ISaleRepository saleRepository, ICustomerService customerService, IProductService productService) {
        this.saleRepository = saleRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    @Override
    public SaleModel saveSale(SaleDto saleDto) {
        var sale = new SaleModel();
        UUID customerId = saleDto.clientId();

        if(customerId != null) {
            this.handlerCustomerExists(customerId);
        }

        this.updateStockQuantity(saleDto.productsSold());

        List<ProductModel> products = this.getProducts(saleDto.productsSold());

        BeanUtils.copyProperties(saleDto, sale, "productsSold");

        sale.setProducts(products);

        return saleRepository.save(sale);
    }

    @Override
    public List<SaleModel> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public SaleModel updateSale(SaleDto saleDto, UUID id) {
        var sale = getSaleById(id);

        ApplicationUtils.copyNonNullProperties(saleDto, sale);

        return saleRepository.save(sale);
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

    private void handlerCustomerExists(UUID customerId) {
        boolean customerExists = customerService.customerExists(customerId);
        if(!customerExists) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
    }

    private List<ProductModel> getProducts(List<ProductSold> productsSold) {
        List<ProductModel> products = new ArrayList<>(productsSold.size());

        for (ProductSold productSold : productsSold) {
            var productModel = productService.getProductById(productSold.id());

            int stockQuantity = productModel.getStockQuantity();
            int quantitySold = productSold.quantity();

            this.verifyIfExistsSufficientQuantityInStock(stockQuantity, quantitySold);

            productModel.setStockQuantity(stockQuantity - quantitySold);

            productService.updateProduct(productModel);

            products.add(productModel);
        }

        return products;
    }

    private void updateStockQuantity(List<ProductSold> productsSold) {

    }

    private void verifyIfExistsSufficientQuantityInStock(int stockQuantity, int quantitySold) {
        if(stockQuantity - quantitySold < 0) {
            throw new InsufficientStockException("Não há produtos em estoque o suficiente, há somente: " + stockQuantity);
        }
    }
}
