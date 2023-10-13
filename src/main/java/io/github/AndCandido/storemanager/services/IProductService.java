package io.github.AndCandido.storemanager.services;

import io.github.AndCandido.storemanager.dtos.ProductDto;
import io.github.AndCandido.storemanager.models.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IProductService {
    Product saveProduct(ProductDto productDto);
    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Product updateProduct(ProductDto productDto, UUID id) throws Exception;

}
