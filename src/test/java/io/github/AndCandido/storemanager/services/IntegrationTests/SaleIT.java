package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.requests.*;
import io.github.AndCandido.storemanager.domain.dtos.responses.*;
import io.github.AndCandido.storemanager.services.creators.*;
import io.github.AndCandido.storemanager.services.dataTest.ProductDataTest;
import io.github.AndCandido.storemanager.services.dataTest.CustomerDataTest;
import io.github.AndCandido.storemanager.services.dataTest.InstallmentDataTest;
import io.github.AndCandido.storemanager.services.dataTest.ProductSoldDataTest;
import io.github.AndCandido.storemanager.services.dataTest.SaleDataTest;
import io.github.AndCandido.storemanager.services.auth.BasicAuthTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class SaleIT {

    private final BasicAuthTest basicAuthTest;
    private TestRestTemplate restTemplate;
    private final ProductDataTest productData;
    private final CustomerDataTest customerData;
    private final ProductSoldDataTest productSoldData;
    private final InstallmentDataTest installmentData;
    private final SaleDataTest saleData;

    @Autowired
    public SaleIT(BasicAuthTest basicAuthTest, TestRestTemplate restTemplate, ProductDataTest productData, CustomerDataTest customerData, ProductSoldDataTest productSoldData, InstallmentDataTest installmentData, SaleDataTest saleData) {
        this.basicAuthTest = basicAuthTest;
        this.restTemplate = restTemplate;
        this.productData = productData;
        this.productSoldData = productSoldData;
        this.customerData = customerData;
        this.installmentData = installmentData;
        this.saleData = saleData;
    }
    @BeforeEach
    void setDatas() {
        setBasicAuth();
        productData
            .createRequestProducts()
            .setProductsDtoSaved(saveAllProducts());
        productSoldData.createRequestProductsSold(productData.getProductsDtoSaved());
        customerData
            .createRequestCustomers()
            .setCustomersSaved(saveAllCustomers());
        installmentData.createInstallments();
        saleData.createSales(
            customerData.getCustomerResponseDto(0).id(),
            productSoldData.getProductsSoldRequestDto(),
            installmentData.getInstallmentsRequestDto()
        );
    }

    private void setBasicAuth() {
        this.restTemplate = restTemplate.withBasicAuth(basicAuthTest.getUsername(), basicAuthTest.getPassword());
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

    private List<CustomerResponseDto> saveAllCustomers() {
        List<CustomerResponseDto> customersDtoSaved = new ArrayList<>(customerData.getCustomersRequestDto().size());

        for (CustomerRequestDto customerRequestDto : customerData.getCustomersRequestDto()) {
            ResponseEntity<CustomerResponseDto> responseCustomerSaved = restTemplate.postForEntity(
                CustomerDataTest.CUSTOMERS_URI, customerRequestDto, CustomerResponseDto.class
            );

            assertEquals(HttpStatus.CREATED, responseCustomerSaved.getStatusCode());

            customersDtoSaved.add(responseCustomerSaved.getBody());
        }

        return customersDtoSaved;
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
        customerData.cleanDataBase();
        saleData.cleanDataBase();
    }

    @Test
    public void saveSale_Success() {
        List<SaleResponseDto> salesDtoSaved = saveAllSales();

        assertionsNotNullBodySaleList(salesDtoSaved);
        assertionsBodySaleList(salesDtoSaved);
    }

    private List<SaleResponseDto> saveAllSales() {
        List<SaleResponseDto> salesDtoSaved = new ArrayList<>(saleData.getSalesRequestDto().size());

        for (SaleRequestDto saleRequestDto : saleData.getSalesRequestDto()) {
            ResponseEntity<SaleResponseDto> responseSalesSaved = restTemplate.postForEntity(
                SaleDataTest.SALES_URI, saleRequestDto, SaleResponseDto.class
            );

            assertEquals(HttpStatus.CREATED, responseSalesSaved.getStatusCode());

            salesDtoSaved.add(responseSalesSaved.getBody());
        }

        return salesDtoSaved;
    }

    @Test
    public void getAllSales_Success() {
        saveAllSales();

        ResponseEntity<List<SaleResponseDto>> res = findAllSales();
        List<SaleResponseDto> body = res.getBody();
        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        assertionsNotNullBodySaleList(body);
        assertionsBodySaleList(body);
    }

    private ResponseEntity<List<SaleResponseDto>> findAllSales() {
        return restTemplate.exchange(
            SaleDataTest.SALES_URI,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<SaleResponseDto>>() {}
        );
    }

    @Test
    public void getSaleById_Success() {
        List<SaleResponseDto> salesDtoSaved = saveAllSales();

        for (SaleResponseDto saleResponseDto : salesDtoSaved) {
            ResponseEntity<SaleResponseDto> res = findSaleById(saleResponseDto.id());

            Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
            var body = res.getBody();

            assertionsNotNullBodySale(body);
            assertionsBodySale(body);
        }
    }

    private ResponseEntity<SaleResponseDto> findSaleById(UUID id) {
        return restTemplate.getForEntity(
            SaleDataTest.SALES_URI + "/" + id,
            SaleResponseDto.class
        );
    }

    @Test
    public void updateSale_Success() {
        List<SaleResponseDto> saleDtoSaved = saveAllSales();
        SaleResponseDto saleSaved = saleDtoSaved.get(0);

        List<ProductResponseDto> productsOriginal = productData.getProductsDtoSaved();
        List<ProductSoldResponseDto> productsSoldOriginal = saleSaved.productsSold();

        List<ProductSoldRequestDto> productsSoldDtoForUpdate = List.of(
            createProductSoldDto(productData.getProductsDtoSaved().get(0).id(), productData.getProductRequestDto(0).stockQuantity() - 1),
            createProductSoldDto(productData.getProductsDtoSaved().get(1).id(), productData.getProductRequestDto(1).stockQuantity()),
            createProductSoldDto(productData.getProductsDtoSaved().get(3).id(), productData.getProductRequestDto(3).stockQuantity() - 1),
            createProductSoldDto(productData.getProductsDtoSaved().get(4).id(), productData.getProductRequestDto(4).stockQuantity())
        );

        SaleRequestDto saleRequestDtoForUpdate = createSaleDtoForUpdate(
            customerData.getCustomerResponseDto(0).id(),
            productsSoldDtoForUpdate,
            installmentData.getInstallmentsRequestDto()
        );

        ResponseEntity<SaleResponseDto> responseSaleUpdate = restTemplate.exchange(
            SaleDataTest.SALES_URI + "/" + saleSaved.id(),
            HttpMethod.PUT,
            new HttpEntity<>(saleRequestDtoForUpdate),
            SaleResponseDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, responseSaleUpdate.getStatusCode());

        SaleResponseDto saleUpdated = responseSaleUpdate.getBody();
        Assertions.assertNotNull(saleUpdated);
        List<ProductSoldResponseDto> productsSoldCurrent = saleUpdated.productsSold();

        assertionsNotNullBodySale(saleUpdated);
        Assertions.assertEquals(saleSaved.id(), saleUpdated.id());
        Assertions.assertEquals(saleSaved.createdAt(), saleUpdated.createdAt());
        Assertions.assertEquals(productsSoldDtoForUpdate.size(), productsSoldCurrent.size());


        ResponseEntity<List<ProductResponseDto>> responseProductsCurrent = findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, responseProductsCurrent.getStatusCode());
        List<ProductResponseDto> productsCurrent = responseProductsCurrent.getBody();

        Assertions.assertNotNull(productsCurrent);

        for (ProductResponseDto productOriginal : productsOriginal) {
            ProductResponseDto productCurrent = getProductCurrent(productsCurrent, productOriginal);
            ProductSoldResponseDto productSoldOriginal = getProductSoldOriginal(productsSoldOriginal, productOriginal);
            ProductSoldResponseDto productSoldCurrent = getProductSoldCurrent(productsSoldCurrent, productOriginal);

            if (productSoldOriginal == null && productSoldCurrent == null) {
                continue;
            }

            assert productCurrent != null;

            if (productSoldOriginal != null) {
                if (productSoldCurrent != null) {
                    Assertions.assertEquals(
                        productOriginal.stockQuantity() - productSoldCurrent.quantity(),
                        productCurrent.stockQuantity()
                    );
                    continue;
                }

                Assertions.assertEquals(
                    productOriginal.stockQuantity(),
                    productCurrent.stockQuantity()
                );
                continue;
            }

            Assertions.assertEquals(
                productOriginal.stockQuantity() - productSoldCurrent.quantity(),
                productCurrent.stockQuantity()
            );
        }
    }

    private ProductSoldResponseDto getProductSoldCurrent(
        List<ProductSoldResponseDto> productsSoldCurrent,
        ProductResponseDto productOriginal
    ) {
        for (ProductSoldResponseDto productSoldCurrent : productsSoldCurrent) {
            if(productSoldCurrent.productId().equals(productOriginal.id())) {
                return productSoldCurrent;
            }
        }

        return null;
    }

    private ProductSoldResponseDto getProductSoldOriginal(
        List<ProductSoldResponseDto> productsSoldOriginal,
        ProductResponseDto productOriginal
    ) {
        for (ProductSoldResponseDto productSoldOriginal : productsSoldOriginal) {
            if(productSoldOriginal.productId().equals(productOriginal.id())) {
                return productSoldOriginal;
            }
        }

        return null;
    }

    private ProductSoldRequestDto getProductSoldOfProduct(
        List<ProductSoldRequestDto> productsSoldDtoForUpdate, ProductResponseDto productCurrent
    ) {
        return productsSoldDtoForUpdate.stream()
            .filter(productSold -> productSold.productId().equals(productCurrent.id()))
            .findFirst()
            .orElse(null);
    }

    private ProductResponseDto getProductCurrent(
        List<ProductResponseDto> productsCurrent,
        ProductResponseDto productOriginal
    ) {
        for (ProductResponseDto productCurrent : productsCurrent) {
            if(productCurrent.id().equals(productOriginal.id())) {
                return productCurrent;
            }
        }

        return null;
    }

    private ResponseEntity<List<ProductResponseDto>> findAllProducts() {
        return restTemplate.exchange(
            ProductDataTest.PRODUCTS_URI,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProductResponseDto>>() {}
        );
    }

    @Test
    public void deleteSale_Success() {
        List<SaleResponseDto> saleDtoSaved = saveAllSales();
        UUID saleIdToDelete = saleDtoSaved.get(0).id();

        ResponseEntity<Void> res = restTemplate.exchange(
            SaleDataTest.SALES_URI + "/" + saleIdToDelete,
            HttpMethod.DELETE,
            null,
            Void.class
        );
        Assertions.assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());

        ResponseEntity<SaleResponseDto> responseSaleDeleted = findSaleById(saleIdToDelete);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaleDeleted.getStatusCode());

        for (ProductSoldRequestDto productSoldRequestDto : productSoldData.getProductsSoldRequestDto()) {
            Long productId = productSoldRequestDto.productId();
            ProductResponseDto productOriginal = productData.getProductsDtoSaved().stream()
                .filter(productDto -> Objects.equals(productId, productDto.id()))
                .findFirst()
                .orElse(null);

            ResponseEntity<ProductResponseDto> productUpdated = findProductById(productId);
            Assertions.assertEquals(HttpStatus.OK, productUpdated.getStatusCode());

            assert productOriginal != null;
            Assertions.assertEquals(productOriginal.stockQuantity(), productUpdated.getBody().stockQuantity());
        }
    }

    private ResponseEntity<ProductResponseDto> findProductById(Long productId) {
        return restTemplate.getForEntity(
            ProductDataTest.PRODUCTS_URI + "/" + productId,
            ProductResponseDto.class
        );
    }

    private void assertionsBodySale(SaleResponseDto body) {
        ResponseEntity<List<ProductResponseDto>> responseGetAllProducts = findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, responseGetAllProducts.getStatusCode());

        List<ProductResponseDto> productsDto = responseGetAllProducts.getBody();
        Assertions.assertNotNull(productsDto);

        HashMap<Long, ProductResponseDto> productsMappedUpdated = new HashMap<>();

        instanceProductsMapped(productsMappedUpdated, productsDto);

        assertionsProductSold(body, productsMappedUpdated);
        assertionsInstallments(body);
    }

    private void assertionsBodySaleList(List<SaleResponseDto> body) {
        for (SaleResponseDto saleBody : body) {
            assertionsBodySale(saleBody);
        }
    }

    private void instanceProductsMapped(HashMap<Long, ProductResponseDto> productsMapped, List<ProductResponseDto> productsDto) {
        for (ProductResponseDto productResponseDto : productsDto) {
            productsMapped.put(productResponseDto.id(), productResponseDto);
        }
    }

    private void assertionsProductSold(SaleResponseDto body, HashMap<Long, ProductResponseDto> productsMappedUpdated) {
        HashMap<Long, ProductResponseDto> productsMappedOriginal = new HashMap<>();
        productData.getProductsDtoSaved().forEach(productDto -> {
            productsMappedOriginal.put(productDto.id(), productDto);
        });

        for (ProductSoldResponseDto productSoldResponseDto : body.productsSold()) {
            var productId = productSoldResponseDto.productId();
            Assertions.assertNotNull(productId);

            var productOriginal = productsMappedOriginal.get(productId);
            var productUpdated = productsMappedUpdated.get(productId);

            var stockQuantityOriginal = productOriginal.stockQuantity();
            var stockQuantityUpdated = productUpdated.stockQuantity();

            Assertions.assertEquals(stockQuantityOriginal - productSoldResponseDto.quantity(), stockQuantityUpdated);
        }
    }

    private void assertionsInstallments(SaleResponseDto body) {
        for (InstallmentResponseDto installment : body.installments()) {
            Assertions.assertNotNull(installment);
        }
    }

    private void assertionsNotNullBodySale(SaleResponseDto body) {
        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.id());
        Assertions.assertNotNull(body.price());
        Assertions.assertNotNull(body.customer());
        Assertions.assertNotNull(body.installments());
        Assertions.assertNotNull(body.productsSold());
        Assertions.assertNotNull(body.createdAt());
    }

    private void assertionsNotNullBodySaleList(List<SaleResponseDto> body) {
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isEmpty());

        for (SaleResponseDto saleBody : body) {
            assertionsNotNullBodySale(saleBody);
        }
    }

    private ProductSoldRequestDto createProductSoldDto(Long id, int quantity) {
        return ProductSoldCreator.createProductSoldRequestDto(id, quantity);
    }

    private SaleRequestDto createSaleDtoForUpdate(
        UUID customerId,
        List<ProductSoldRequestDto> productsSoldDtoForUpdate,
        List<InstallmentRequestDto> installmentsDto
    ) {
        return SaleCreator.createSaleRequestDto(customerId, 1000.00, productsSoldDtoForUpdate, installmentsDto);
    }

    private ProductResponseDto getProductOn(ProductResponseDto productModel, List<ProductResponseDto> productsForIterate) {
        for (ProductResponseDto productResponseDto : productsForIterate) {
            if (Objects.equals(productResponseDto.id(), productModel.id()))
                return productResponseDto;
        }

        return null;
    }
}