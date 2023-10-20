package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.utils.ApplicationUtils;
import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public ProductModel updateProduct(ProductDto productDto, Long id) {
        ProductModel productFound = getProductById(id);

        ApplicationUtils.copyNonNullProperties(productDto, productFound);

        ProductModel productSaved = productRepository.save(productFound);
        return productSaved;
    }

    @Override
    public ProductModel updateProduct(ProductModel productModel) {
        return productRepository.save(productModel);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductModel product = getProductById(id);
        productRepository.delete(product);
    }

    @Override
    public ProductModel getProductById(Long id) {
        ProductModel product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        return product;
    }

    @Override
    public List<ProductModel> updateAllProducts(List<ProductModel> productsModel) {
        return productRepository.saveAll(productsModel);
    }
}
