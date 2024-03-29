package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductSoldResponseDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductSoldServiceImpl implements IProductSoldService {

    private final IProductSoldRepository productSoldRepository;
    private final IProductService productService;

    @Override
    public ProductSold createProductSold(ProductSoldRequestDto productSoldRequestDto) {
        var product = productService.getProductById(productSoldRequestDto.productId());
        var quantitySold = productSoldRequestDto.quantity();

        productService.updatedStockQuantityByProductSold(product, quantitySold);

        var productSold = new ProductSold();
        productSold.setProduct(product);
        productSold.setQuantity(quantitySold);

        return productSold;
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
