package io.github.AndCandido.storemanager.services.impl;

import io.github.AndCandido.storemanager.utils.ApplicationUtils;
import io.github.AndCandido.storemanager.dtos.ProductDto;
import io.github.AndCandido.storemanager.exceptions.product.ProductNotFoundException;
import io.github.AndCandido.storemanager.models.Product;
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
    public Product saveProduct(ProductDto productDto) {
        Product productModel = new Product();
        BeanUtils.copyProperties(productDto, productModel);

        Product productSaved = productRepository.save(productModel);
        return productModel;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(ProductDto productDto, UUID id) throws Exception {

        Optional<Product> productFound = productRepository.findById(id);

        if(productFound.isEmpty()) {
            throw new Exception("Produto não encontrado");
        }

        ApplicationUtils.copyNonNullProperties(productDto, productFound);

        Product productSaved = productRepository.save(productFound.get());
        return productSaved;
    }

    @Override
    public Product getProductById(UUID id) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty()) {
            throw new ProductNotFoundException("Produto não encontrado");
        }

        return product.get();
    }
}
