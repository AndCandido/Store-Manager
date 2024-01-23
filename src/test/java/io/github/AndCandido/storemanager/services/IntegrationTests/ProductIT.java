package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;
import io.github.AndCandido.storemanager.services.auth.BasicAuthTest;
import io.github.AndCandido.storemanager.services.dataTest.ProductDataTest;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ProductIT {

    private final BasicAuthTest basicAuthTest;
    private final ProductDataTest productData;
    private TestRestTemplate restTemplate;

    @Autowired
    public ProductIT(BasicAuthTest basicAuthTest, ProductDataTest productDataTest, TestRestTemplate restTemplate) {
        this.basicAuthTest = basicAuthTest;
        this.productData = productDataTest
            .createRequestProducts();
        this.restTemplate = restTemplate;
    }

    @BeforeEach
    void setBasicAuth() {
        this.restTemplate = restTemplate.withBasicAuth(basicAuthTest.getUsername(), basicAuthTest.getPassword());
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
    }

    @Test
    public void saveProducts_Success() {
        for (ProductRequestDto productRequestDto : productData.getProductsRequestDto()) {
            ResponseEntity<ProductResponseDto> responseProductSave =
                restTemplate.postForEntity(
                    ProductDataTest.PRODUCTS_URI,
                    productRequestDto,
                    ProductResponseDto.class
                );

            assertEquals(HttpStatus.CREATED, responseProductSave.getStatusCode());
            ProductResponseDto bodyProductResponse = responseProductSave.getBody();

            assertionsProductBody(productRequestDto, bodyProductResponse);
        }
    }

    private void assertionsProductBody(ProductRequestDto productRequestDto, ProductResponseDto bodyProductResponse) {
        assertNotNull(bodyProductResponse);
        assertNotNull(bodyProductResponse.id());
        assertNotNull(bodyProductResponse.createdAt());
        assertEquals(productRequestDto.name(), bodyProductResponse.name());
        assertEquals(productRequestDto.price(), bodyProductResponse.price());
        assertEquals(productRequestDto.stockQuantity(), bodyProductResponse.stockQuantity());
    }

    @Test
    public void getAllProducts_Success() {
        saveAllProducts();

        ResponseEntity<List<ProductResponseDto>> responseGetAllProducts =
            restTemplate.exchange(
                ProductDataTest.PRODUCTS_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
            );

        assertEquals(HttpStatus.OK, responseGetAllProducts.getStatusCode());
        List<ProductResponseDto> body = responseGetAllProducts.getBody();

        assertNotNull(body);
        assertEquals(body.size(), productData.getProductsRequestDto().size());

        for (int i = 0; i < body.size(); i++) {
            var productSaved = productData.getProductRequestDto(i);
            var productBody = body.get(i);

            assertionsProductBody(productSaved, productBody);
        }
    }

    private List<ProductResponseDto> saveAllProducts() {
        List<ProductResponseDto> productsDtoSaved = new ArrayList<>(productData.getProductsRequestDto().size());

        for (ProductRequestDto productRequestDto : productData.getProductsRequestDto()) {
            ResponseEntity<ProductResponseDto> responseProductSaved = restTemplate.postForEntity(
                ProductDataTest.PRODUCTS_URI, productRequestDto, ProductResponseDto.class
            );

            assertEquals(HttpStatus.CREATED, responseProductSaved.getStatusCode());

            productsDtoSaved.add(responseProductSaved.getBody());
        }

        return productsDtoSaved;
    }

    @Test
    public void getAllProducts_NoContent() {
        ResponseEntity<List<ProductResponseDto>> responseGetAllProducts =
            restTemplate.exchange(
                ProductDataTest.PRODUCTS_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
            );

        assertEquals(HttpStatus.OK, responseGetAllProducts.getStatusCode());
        List<ProductResponseDto> body = responseGetAllProducts.getBody();

        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @Test
    public void updateProducts_Success() {
        List<ProductResponseDto> productsDtoSaved = saveAllProducts();

        for (ProductResponseDto productResponseDto : productsDtoSaved) {
            var productDtoToUpdate = ProductResponseDto.builder()
                    .name("CHANGED " + productResponseDto.name())
                    .price(productResponseDto.price() + 10)
                    .stockQuantity(productResponseDto.stockQuantity() + 5)
                    .build();

            ResponseEntity<ProductResponseDto> responsePutProduct =
                restTemplate.exchange(
                    ProductDataTest.PRODUCTS_URI + "/" + productResponseDto.id(),
                    HttpMethod.PUT,
                    new HttpEntity<>(productDtoToUpdate),
                    ProductResponseDto.class
                );

            assertEquals(HttpStatus.CREATED, responsePutProduct.getStatusCode());
            ProductResponseDto body = responsePutProduct.getBody();

            assertNotNull(body);
            assertEquals(productResponseDto.id(), body.id());
            assertEquals(productResponseDto.createdAt(), body.createdAt());
            assertEquals(productDtoToUpdate.name(), body.name());
            assertEquals(productDtoToUpdate.price(), body.price());
            assertEquals(productDtoToUpdate.stockQuantity(), body.stockQuantity());
        }
    }

    @Test
    public void deleteAllProduct_Success() {
        List<ProductResponseDto> productsSaved = saveAllProducts();

        for (ProductResponseDto productSaved : productsSaved) {
            ResponseEntity<Void> deleteResponse =
                restTemplate.exchange(
                    ProductDataTest.PRODUCTS_URI + "/" + productSaved.id(),
                    HttpMethod.DELETE,
                    null,
                    Void.class
                );

            assertNull(deleteResponse.getBody());
            assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

            ResponseEntity<ProductResponseDto> responseProductDeleted =
                restTemplate.getForEntity(ProductDataTest.PRODUCTS_URI + "/" + productSaved.id(), ProductResponseDto.class);
            assertEquals(HttpStatus.NOT_FOUND, responseProductDeleted.getStatusCode());
        }
    }
}