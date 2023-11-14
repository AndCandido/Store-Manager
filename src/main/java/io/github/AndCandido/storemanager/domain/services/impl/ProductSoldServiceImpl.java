package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductSoldServiceImpl implements IProductSoldService {

    private IProductSoldRepository productSoldRepository;

    public ProductSoldServiceImpl(IProductSoldRepository productSoldRepository) {
        this.productSoldRepository = productSoldRepository;
    }

    @Override
    public List<ProductSoldModel> getProductSoldByProduct(ProductModel productModel) {
        var productsSoldModel = productSoldRepository.findByProductModel(productModel);
        return productsSoldModel;
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
