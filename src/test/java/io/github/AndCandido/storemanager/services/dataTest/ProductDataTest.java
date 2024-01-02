package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;

import static io.github.AndCandido.storemanager.services.creators.ProductCreator.*;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.services.restTemplates.ResourceTestRestTemplate;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class ProductDataTest {
    private static final String PRODUCTS_URI = "/products";

    private List<ProductDto> productsDto;

    private final ResourceTestRestTemplate<ProductDto, Long> restTemplate;
    private final IProductRepository productRepository;

    public ProductDataTest(ResourceTestRestTemplate<ProductDto, Long> restTemplate, IProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }

    public ProductDataTest createProducts() {
        productsDto = Arrays.asList(
            createProductDto("Meia", 18.99, 5),
            createProductDto("Calça", 145.00, 1),
            createProductDto("Camisa", 45.50, 8),
            createProductDto("Blusa", 45.05, 21),
            createProductDto("Saia", 122, 50)
        );

        return this;
    }

    public ProductDataTest saveProducts() {
        productsDto = restTemplate
            .postAll(PRODUCTS_URI, productsDto, new ParameterizedTypeReference<ProductDto>() {}).stream()
            .map(response -> {
                Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
                return response.getBody();
            })
            .toList();

        return this;
    }

    public ProductDto getProductDto(int index) {
        return productsDto.get(index);
    }

    public List<ResponseEntity<ProductDto>> findProductsByProductSold(List<ProductSoldDto> productsSoldDto) {
        List<ResponseEntity<ProductDto>> responses = new ArrayList<>(productsSoldDto.size());

        for (ProductSoldDto productSoldDto : productsSoldDto) {
            ResponseEntity<ProductDto> response = restTemplate
                .getById(PRODUCTS_URI, productSoldDto.productId(), new ParameterizedTypeReference<>() {});

            responses.add(response);
        }

        return responses;
    }

    public void cleanDataBase() {
        productRepository.deleteAll();
    }

    public ResponseEntity<ProductDto> saveProduct(ProductDto product) {
        return restTemplate.post(PRODUCTS_URI, product, new ParameterizedTypeReference<ProductDto>() {});
    }

    public ResponseEntity<Void> deleteProduct(ProductDto productDto) {
        return restTemplate.delete(PRODUCTS_URI, productDto.id());
    }

    public ResponseEntity<List<ProductDto>> findAllProducts() {
        return restTemplate.getAll(PRODUCTS_URI, new ParameterizedTypeReference<List<ProductDto>>() {});
    }

    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDtoToUpdate) {
        return restTemplate.put(PRODUCTS_URI, id, productDtoToUpdate, new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<ProductDto> findProduct(ProductDto productDto) {
        return findProductById(productDto.id());
    }

    public ResponseEntity<ProductDto> findProductById(Long productId) {
        return restTemplate.getById(PRODUCTS_URI, productId, new ParameterizedTypeReference<ProductDto>() {});
    }
}
