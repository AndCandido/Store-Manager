package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.services.utils.ProductCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ProductIT {

    private final String uri = "/products";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IProductRepository productRepository;

    @Test
    public void saveProducts_Success() {
        ProductDto[] products = {
                ProductCreator.createProductDto("Calça", 129, 5),
                ProductCreator.createProductDto("Camisa", 90.05, 1),
                ProductCreator.createProductDto("Blusa", 102.99, 6)
        };

        for (ProductDto product : products) {
            var response = restTemplate.postForEntity(uri, product, ProductModel.class);
            var body = response.getBody();

            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            Assertions.assertNotNull(body);
            Assertions.assertNotNull(body.getId());
            Assertions.assertNotNull(body.getCreatedAt());

            Assertions.assertEquals(product.name(), body.getName());
            Assertions.assertEquals(product.price(), body.getPrice());
            Assertions.assertEquals(product.stockQuantity(), body.getStockQuantity());
        }

        productRepository.deleteAll();
    }

    @Test
    public void getAllProducts_Success() {
        ProductModel[] productsSaved = {
            ProductCreator.createProductModel("Meia", 18.99, 5),
            ProductCreator.createProductModel("Meia", 18.99, 5)
        };

        productRepository.saveAll(Arrays.stream(productsSaved).toList());

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductModel>>() {}
        );

        var body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(body.size(), productsSaved.length);

        for (int i = 0; i < body.size(); i++) {
            var productSaved = productsSaved[i];
            var productBody = body.get(i);

            Assertions.assertNotNull(productBody.getId());
            Assertions.assertNotNull(productBody.getCreatedAt());

            Assertions.assertEquals(productSaved.getName(), productBody.getName());
            Assertions.assertEquals(productSaved.getPrice(), productBody.getPrice());
            Assertions.assertEquals(productSaved.getStockQuantity(), productBody.getStockQuantity());

        }

        productRepository.deleteAll();
    }

    @Test
    public void getAllProducts_NoContent() {
        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductModel>>() {}
        );

        var body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(body.isEmpty());
    }

    @Test
    public void updateProduct_Success() {
        var productSaved = productRepository.save(
                ProductCreator.createProductModel("Bolsa", 13, 3)
        );

        var productForUpdate = ProductCreator.createProductDto(
                "Bolsa", 13.05, 3
        );

        var productSavedId = productSaved.getId();

        var response = restTemplate.exchange(
                getUriWithId(productSavedId),
                HttpMethod.PUT,
                new HttpEntity<ProductDto>(productForUpdate),
                ProductModel.class
        );

        var body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Assertions.assertEquals(productSavedId, body.getId());
        Assertions.assertEquals(productSaved.getCreatedAt(), body.getCreatedAt());
        Assertions.assertEquals(productForUpdate.name(), body.getName());
        Assertions.assertEquals(productForUpdate.price(), body.getPrice());
        Assertions.assertEquals(productForUpdate.stockQuantity(), body.getStockQuantity());

        productRepository.deleteAll();
    }

    @Test
    public void deleteProduct_Success() {
        var productSaved = productRepository.save(
                ProductCreator.createProductModel("Meia", 12.99, 2)
        );

        var productSavedId = productSaved.getId();
        var response = restTemplate.exchange(
                getUriWithId(productSavedId),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        Assertions.assertNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        var productDeletedFound = productRepository.findById(productSavedId);
        Assertions.assertTrue(productDeletedFound.isEmpty());
    }
    private String getUriWithId(Long id) {
        return uri + "/" + id;
    }
}
