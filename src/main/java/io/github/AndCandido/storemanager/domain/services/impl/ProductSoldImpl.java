package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.UUID;

public class ProductSoldImpl implements IProductSoldService {

    private IProductSoldRepository productSoldRepository;

    public ProductSoldImpl(IProductSoldRepository productSoldRepository) {
        this.productSoldRepository = productSoldRepository;
    }

    @Override
    public ProductSoldModel saveProductSold(@Valid ProductSoldDto productSoldDto) {
        var productSoldModel = new ProductSoldModel();

        BeanUtils.copyProperties(productSoldDto, productSoldModel);

        return productSoldRepository.save(productSoldModel);
    }

    @Override
    public List<ProductSoldModel> getProductsSold() {
        return productSoldRepository.findAll();
    }

    @Override
    public ProductSoldModel updateProductSold(ProductSoldDto productSoldDto, UUID id) {
        var productSoldModel = getProductSoldById(id);

        BeanUtils.copyProperties(productSoldDto, productSoldModel);

        return productSoldRepository.save(productSoldModel);
    }

    @Override
    public void deleteProductSold(UUID id) {
        var productSoldModel = getProductSoldById(id);
        productSoldRepository.delete(productSoldModel);
    }

    @Override
    public ProductSoldModel getProductSoldById(UUID id) {
        return productSoldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto vendido não encontrado"));
    }
}
