package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductSoldServiceImpl implements IProductSoldService {

    private IProductSoldRepository productSoldRepository;
    private IProductService productService;

    public ProductSoldServiceImpl(IProductSoldRepository productSoldRepository, IProductService productService) {
        this.productSoldRepository = productSoldRepository;
        this.productService = productService;
    }

    @Override
    @Transactional
    public List<ProductSold> saveProductsSoldBySale(Sale sale, SaleDto saleDto) {
        var productsSold = new ArrayList<ProductSold>(saleDto.productsSold().size());

        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var productSold = new ProductSold();
            var product = productService.getProductById(productSoldDto.productId());
            var quantitySold = productSoldDto.quantity();

            productSold.setProduct(product);
            productSold.setSale(sale);
            productSold.setQuantity(quantitySold);

            productService.updatedStockQuantityByProductSold(product, quantitySold);
            productsSold.add(productSold);
        }

        return productsSold;
    }

    @Override
    public void deleteProductSold(UUID id) {
        var productSoldModel = getProductSoldById(id);
        productSoldRepository.delete(productSoldModel);
    }

    @Override
    public ProductSold getProductSoldById(UUID id) {
        return productSoldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto vendido não encontrado"));
    }
}
