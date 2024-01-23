package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;

import io.github.AndCandido.storemanager.services.creators.ProductCreator;

import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class ProductDataTest {
    public static final String PRODUCTS_URI = "/products";

    @Getter
    private List<ProductRequestDto> productsRequestDto;

    @Getter@Setter
    private List<ProductResponseDto> productsDtoSaved;

    private final IProductRepository productRepository;

    public ProductDataTest(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDataTest createRequestProducts() {
        productsRequestDto = Arrays.asList(
            ProductCreator.createProductRequestDto("Meia", 18.99, 5),
            ProductCreator.createProductRequestDto("Calça", 145.00, 1),
            ProductCreator.createProductRequestDto("Camisa", 45.50, 8),
            ProductCreator.createProductRequestDto("Blusa", 45.05, 21),
            ProductCreator.createProductRequestDto("Saia", 122, 50)
        );

        return this;
    }

    public ProductRequestDto getProductRequestDto(int index) {
        return productsRequestDto.get(index);
    }

    public ProductResponseDto getProductSaved(int index) {
        return productsDtoSaved.get(index);
    }

    public void cleanDataBase() {
        productRepository.deleteAll();
    }
}
