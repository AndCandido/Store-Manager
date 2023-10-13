package io.github.AndCandido.storemanager.services.impl;

import io.github.AndCandido.storemanager.utils.ApplicationUtils;
import io.github.AndCandido.storemanager.dtos.ProductDto;
import io.github.AndCandido.storemanager.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.models.ProductModel;
import io.github.AndCandido.storemanager.repositories.IProductRepository;
import io.github.AndCandido.storemanager.services.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public ProductModel saveProduct(ProductDto productDto) {
        ProductModel productModel = new ProductModel();
        BeanUtils.copyProperties(productDto, productModel);

        ProductModel productSaved = productRepository.save(productModel);
        return productModel;
    }

    @Override
    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductModel updateProduct(ProductDto productDto, UUID id) {

        ProductModel productFound = getProductById(id);

        ApplicationUtils.copyNonNullProperties(productDto, productFound);

        ProductModel productSaved = productRepository.save(productFound);
        return productSaved;
    }

    @Override
    public void deleteProduct(UUID id) {
        ProductModel product = getProductById(id);
        productRepository.delete(product);
    }

    @Override
    public ProductModel getProductById(UUID id) {
        ProductModel product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return product;
    }
}
