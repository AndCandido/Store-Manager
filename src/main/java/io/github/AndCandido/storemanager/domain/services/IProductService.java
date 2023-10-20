package io.github.AndCandido.storemanager.domain.services;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;

import java.util.List;

public interface IProductService {
    ProductModel saveProduct(ProductDto productDto);

    List<ProductModel> getAllProducts();

    ProductModel getProductById(Long id);

    ProductModel updateProduct(ProductDto productDto, Long id);

    ProductModel updateProduct(ProductModel productModel);

    void deleteProduct(Long id);

    List<ProductModel> updateAllProducts(List<ProductModel> productsModel);
}