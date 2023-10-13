package io.github.AndCandido.storemanager.services;

import io.github.AndCandido.storemanager.dtos.ProductDto;
import io.github.AndCandido.storemanager.models.ProductModel;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    ProductModel saveProduct(ProductDto productDto);

    List<ProductModel> getAllProducts();

    ProductModel getProductById(UUID id);

    ProductModel updateProduct(ProductDto productDto, UUID id);

    void deleteProduct(UUID id);
}
