package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.models.Product;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.List;

public interface IProductService {
    Product saveProduct(ProductRequestDto productRequestDto);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(ProductRequestDto productRequestDto, Long id);

    void returnStockQuantityByProductSold(ProductSold productSold);

    Product updateProduct(Product product);

    void updatedStockQuantityByProductSold(Product product, int quantitySold);

    void deleteProduct(Long id);
}