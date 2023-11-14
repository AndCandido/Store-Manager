package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IProductSoldService productSoldService;

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
    public ProductModel updateProduct(ProductDto productDto, Long id) {
        ProductModel productFound = getProductById(id);

        ApplicationUtil.copyNonNullProperties(productDto, productFound);

        return productRepository.save(productFound);
    }

    @Override
    public ProductModel updateProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductModel product = getProductById(id);
        var productsSoldModel = productSoldService
                .getProductSoldByProduct(product).stream()
                .map(productSoldModel -> {
                    productSoldModel.setProductModel(null);
                    return null;
                })
                .toList();
        productRepository.delete(product);
    }

    @Override
    public ProductModel getProductById(Long id) {
        ProductModel product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return product;
    }
}
