package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.InsufficientStockException;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.models.Product;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    @Override
    public Product saveProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productRequestDto, product);

        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(ProductRequestDto productRequestDto, Long id) {
        Product productFound = getProductById(id);

        ApplicationUtil.copyNonNullProperties(productRequestDto, productFound);

        return productRepository.save(productFound);
    }

    @Override
    public void updatedStockQuantityByProductSold(Product product, int quantitySold) {
        if (product.getStockQuantity() - quantitySold < 0) {
            throwInsufficientStock(product.getName(), product.getStockQuantity());
        }

        decrementStockQuantityProduct(product, quantitySold);
        updateProduct(product);
    }

    @Override
    public void returnStockQuantityByProductSold(ProductSold productSold) {
        if(productSold.getProduct() == null)
            return;

        var product = productSold.getProduct();
        var stockQuantity = product.getStockQuantity();
        var quantitySold = productSold.getQuantity();
        product.setStockQuantity(stockQuantity + quantitySold);
        updateProduct(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    private void throwInsufficientStock(String productName, Integer stockQuantity) {
        throw new InsufficientStockException(
                "Estoque insuficiente de " + productName + " para realizar esta compra, há " + stockQuantity + " unidade(s)"
        );
    }

    private void decrementStockQuantityProduct(Product product, Integer quantitySold) {
        var stockQuantity = product.getStockQuantity();
        product.setStockQuantity(stockQuantity - quantitySold);
    }
}
