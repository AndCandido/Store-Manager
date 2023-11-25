package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.Product;
import io.github.AndCandido.storemanager.domain.models.ProductSold;

import java.util.List;

public interface IProductService {
    Product saveProduct(ProductDto productDto);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(ProductDto productDto, Long id);

    void returnStockQuantityByProductSold(ProductSold productSold);

    Product updateProduct(Product product);

    void updatedStockQuantityByProductSold(Product product, int quantitySold);

    void deleteProduct(Long id);
}