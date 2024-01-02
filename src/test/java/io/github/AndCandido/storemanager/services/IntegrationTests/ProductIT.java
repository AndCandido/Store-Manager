package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.services.dataTest.ProductDataTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ProductIT {

    private final ProductDataTest productData;

    @Autowired
    public ProductIT(ProductDataTest productDataTest) {
        this.productData = productDataTest
            .createProducts();
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
    }

    @Test
    public void saveProducts_Success() {
        for (ProductDto product : productData.getProductsDto()) {
            ResponseEntity<ProductDto> response = productData.saveProduct(product);
            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            ProductDto body = response.getBody();

            Assertions.assertNotNull(body);
            Assertions.assertNotNull(body.id());
            Assertions.assertNotNull(body.createdAt());

            Assertions.assertEquals(product.name(), body.name());
            Assertions.assertEquals(product.price(), body.price());
            Assertions.assertEquals(product.stockQuantity(), body.stockQuantity());
        }
    }

    @Test
    public void getAllProducts_Success() {
        productData.saveProducts();

        ResponseEntity<List<ProductDto>> response = productData.findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ProductDto> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(body.size(), productData.getProductsDto().size());

        for (int i = 0; i < body.size(); i++) {
            var productSaved = productData.getProductDto(i);
            var productBody = body.get(i);

            Assertions.assertNotNull(productBody.id());
            Assertions.assertNotNull(productBody.createdAt());

            Assertions.assertEquals(productSaved.name(), productBody.name());
            Assertions.assertEquals(productSaved.price(), productBody.price());
            Assertions.assertEquals(productSaved.stockQuantity(), productBody.stockQuantity());
        }
    }

    @Test
    public void getAllProducts_NoContent() {
        ResponseEntity<List<ProductDto>> response = productData.findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<ProductDto> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertTrue(body.isEmpty());
    }

    @Test
    public void updateProducts_Success() {
        productData.saveProducts();

        for (ProductDto productDto : productData.getProductsDto()) {

            var productDtoToUpdate = ProductDto.builder()
                    .name("CHANGED " + productDto.name())
                    .price(productDto.price() + 10)
                    .stockQuantity(productDto.stockQuantity() + 5)
                    .build();

            ResponseEntity<ProductDto> response = productData.updateProduct(productDto.id(), productDtoToUpdate);
            Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
            ProductDto body = response.getBody();

            Assertions.assertNotNull(body);
            Assertions.assertEquals(productDto.id(), body.id());
            Assertions.assertEquals(productDto.createdAt(), body.createdAt());
            Assertions.assertEquals(productDtoToUpdate.name(), body.name());
            Assertions.assertEquals(productDtoToUpdate.price(), body.price());
            Assertions.assertEquals(productDtoToUpdate.stockQuantity(), body.stockQuantity());
        }
    }

    @Test
    public void deleteProduct_Success() {
        productData.saveProducts();

        for (ProductDto productDto : productData.getProductsDto()) {
            ResponseEntity<Void> deleteResponse = productData.deleteProduct(productDto);

            Assertions.assertNull(deleteResponse.getBody());
            Assertions.assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

            ResponseEntity<ProductDto> responseProductDeleted = productData.findProduct(productDto);
            Assertions.assertEquals(HttpStatus.NOT_FOUND, responseProductDeleted.getStatusCode());
        }
    }
}