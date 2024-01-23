package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.requests.CustomerRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.CustomerResponseDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductSoldResponseDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.SaleResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.ProductSoldMapper;
import io.github.AndCandido.storemanager.domain.models.ProductSold;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.services.dataTest.*;
import io.github.AndCandido.storemanager.services.auth.BasicAuthTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ApplicationIT {

    private final BasicAuthTest basicAuthTest;
    private TestRestTemplate restTemplate;
    private final ProductDataTest productData;
    private final ProductSoldDataTest productSoldData;
    private final CustomerDataTest customerData;
    private final InstallmentDataTest installmentData;
    private final SaleDataTest saleData;
    private final IProductSoldRepository productSoldRepository;

    @Autowired
    public ApplicationIT(BasicAuthTest basicAuthTest, TestRestTemplate restTemplate, ProductDataTest productData, CustomerDataTest customerData, ProductSoldDataTest productSoldData, InstallmentDataTest installmentData, SaleDataTest saleData, IProductSoldRepository productSoldRepository) {
        this.basicAuthTest = basicAuthTest;
        this.restTemplate = restTemplate;
        this.productData = productData;
        this.productSoldData = productSoldData;
        this.customerData = customerData;
        this.installmentData = installmentData;
        this.saleData = saleData;
        this.productSoldRepository = productSoldRepository;
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
        installmentData
            .createInstallments();
        saleData
            .createSales(
            customerData.getCustomerResponseDto(0).id(),
            productSoldData.getProductsSoldRequestDto(),
            installmentData.getInstallmentsRequestDto())
            .setSalesSaved(saveAllSales());

        productSoldData.setProductsSoldSaved(findAllProductsSold());
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

    private List<ProductSoldResponseDto> findAllProductsSold() {
        return productSoldRepository.findAll().stream().map(ProductSoldMapper::toDto).toList();
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
        customerData.cleanDataBase();
        saleData.cleanDataBase();
    }

    @Test
    void deleteTwoProducts_ProductIdOnProductSoldIsNull_Success() {
        deleteProduct(productData.getProductSaved(0));
        deleteProduct(productData.getProductSaved(1));

        List<ProductSoldResponseDto> productsSoldWhereProductsHasDeleted = List.of(
                findProductSold(productSoldData.getProductSoldSaved(0)),
                findProductSold(productSoldData.getProductSoldSaved(1))
        );

        for (ProductSoldResponseDto productSoldResponseDto : productsSoldWhereProductsHasDeleted) {
            Assertions.assertNull(productSoldResponseDto.productId());
        }
    }

    private void deleteProduct(ProductResponseDto productSaved) {
        ResponseEntity<Void> response = restTemplate.exchange(
            ProductDataTest.PRODUCTS_URI + "/" + productSaved.id(),
            HttpMethod.DELETE,
            null,
            Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    private ProductSoldResponseDto findProductSold(ProductSoldResponseDto productSoldDto) {
        Optional<ProductSold> productSoldFound = productSoldRepository.findById(productSoldDto.id());
        Assertions.assertTrue(productSoldFound.isPresent());
        return ProductSoldMapper.toDto(productSoldFound.get());
    }

    @Test
    void deleteCustomer_CustomerIdOnSaleIsNull_Success() {
        deleteCustomer(customerData.getCustomerResponseDto(0));
        ResponseEntity<SaleResponseDto> response = findSaleById(saleData.getSaleSaved(0).id());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        SaleResponseDto saleUpdated = response.getBody();

        Assertions.assertNotNull(saleUpdated);
        Assertions.assertNull(saleUpdated.customer());
    }

    private void deleteCustomer(CustomerResponseDto customerResponseDto) {
        ResponseEntity<Void> responseDeleteCustomer = restTemplate.exchange(
            CustomerDataTest.CUSTOMERS_URI + "/" + customerResponseDto.id(),
            HttpMethod.DELETE,
            null,
            Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteCustomer.getStatusCode());
    }

    private ResponseEntity<SaleResponseDto> findSaleById(UUID saleId) {
        return restTemplate.getForEntity(
            SaleDataTest.SALES_URI + "/" + saleId,
            SaleResponseDto.class
        );
    }

    @Test
    void deleteSale_SaleIsNullOnProductAndCustomer_Success() {
        deleteSaleById(saleData.getSaleSaved(0).id());

        List<ProductResponseDto> productsUpdated =
            findProductsByProductSold(productSoldData.getProductsSoldRequestDto())
            .stream()
            .map(response -> {
                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                return response.getBody();
            })
            .toList();

        for (ProductResponseDto productResponseDto : productsUpdated) {
            System.out.println(productResponseDto);
            Assertions.assertNull(productResponseDto.productsSold());
        }

        ResponseEntity<CustomerResponseDto> responseCustomersUpdated = findCustomer(customerData.getCustomerResponseDto(0).id());
        Assertions.assertEquals(HttpStatus.OK, responseCustomersUpdated.getStatusCode());
        CustomerResponseDto customerUpdated = responseCustomersUpdated.getBody();

        assert customerUpdated != null;
        Assertions.assertNull(customerUpdated.sales());
        Assertions.assertNull(customerUpdated.installments());
    }

    private void deleteSaleById(UUID saleId) {
        ResponseEntity<Void> responseDeleteSale = restTemplate.exchange(
            SaleDataTest.SALES_URI + "/" + saleId,
            HttpMethod.DELETE,
            null,
            Void.class
        );

        Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteSale.getStatusCode());
    }

    private List<ResponseEntity<ProductResponseDto>> findProductsByProductSold(
        List<ProductSoldRequestDto> productsSoldRequestDto
    ) {
        List<ResponseEntity<ProductResponseDto>> responses = new ArrayList<>(productsSoldRequestDto.size());

        for (ProductSoldRequestDto productSoldRequestDto : productsSoldRequestDto) {
            ResponseEntity<ProductResponseDto> response =
                restTemplate
                .exchange(
                    ProductDataTest.PRODUCTS_URI + "/" + productSoldRequestDto.productId(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
                );

            responses.add(response);
        }

        return responses;
    }

    private ResponseEntity<CustomerResponseDto> findCustomer(UUID customerID) {
        return restTemplate.getForEntity(
            CustomerDataTest.CUSTOMERS_URI + "/" + customerID,
            CustomerResponseDto.class
        );
    }
}