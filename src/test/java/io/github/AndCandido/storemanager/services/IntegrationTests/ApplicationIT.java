package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.*;
import io.github.AndCandido.storemanager.domain.mappers.ProductSoldMapper;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.services.utils.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
public class ApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IProductSoldRepository productSoldRepository;

    private final String PRODUCTS_URI = "/products";
    private final String CUSTOMERS_URI = "/customers";
    private final String SALES_URI = "/sales";

    private List<ProductDto> products = new ArrayList<>();
    private List<CustomerDto> customers = new ArrayList<>();
    private List<ProductSoldDto> productsSoldDto = new ArrayList<>();
    private List<InstallmentDto> installmentsDto = new ArrayList<>();
    private List<SaleDto> sales = new ArrayList<>();

    @BeforeAll
    void beforeAll() {
        products.addAll(List.of(
                saveProduct(createProductDto("Meia", 18.99, 5)),
                saveProduct(createProductDto("Calça", 145.00, 1)),
                saveProduct(createProductDto("Camisa", 45.50, 8)),
                saveProduct(createProductDto("Blusa", 45.05, 21)),
                saveProduct(createProductDto("Saia", 122, 50))
        ));

        productsSoldDto.addAll(List.of(
                createProductSoldDto(products.get(0), 4),
                createProductSoldDto(products.get(1), 1),
                createProductSoldDto(products.get(2), 4)
        ));

        var tomorrowDate = LocalDate.now().plusDays(1);

        installmentsDto.addAll(List.of(
                createInstallment(tomorrowDate.toString(), 123, "DEBIT_CARD", true),
                createInstallment(tomorrowDate.toString(), 123, "NONE", false)
        ));

        customers.addAll(List.of(
                saveCustomer(createCustomer("Alberto Lima Castro", "021.419.780-84", null, "Rua. ", null)),
                saveCustomer(createCustomer("Augusto Ribeiro Arcanjo ", "021.419.780-84", null, "Rua. ", null)),
                saveCustomer(createCustomer("Júlio Antônia Cardoso Castro", "021.419.780-84", "Comercio do Júlio", "Rua. ", null))
        ));

        sales.addAll(List.of(
                saveSale(createSaleDto(customers.get(0), 149.99, productsSoldDto, installmentsDto)),
                saveSale(createSaleDto(
                        customers.get(1),
                        122,
                        List.of(productsSoldDto.get(2)),
                        List.of(installmentsDto.get(0))
                ))
        ));

        productsSoldDto = productSoldRepository.findAll().stream().map(ProductSoldMapper::toDto).toList();
    }

    @Order(1)
    @Test
    void deleteTwoProducts_ProductIdOnProductSoldNull_Success() {
        deleteProduct(products.get(0));
        deleteProduct(products.get(1));

        var productsSoldWhereProductsHasDeleted = List.of(
                getProductSold(productsSoldDto.get(0)),
                getProductSold(productsSoldDto.get(1))
        );

        for (ProductSoldDto productSoldDto : productsSoldWhereProductsHasDeleted) {
            Assertions.assertNull(productSoldDto.productId());
        }
    }

    @Order(2)
    @Test
    void deleteCustomer_CustomerIdOnSaleNull_Success() {
        deleteCustomer(customers.get(0));
        var saleUpdated = findSaleById(sales.get(0));

        Assertions.assertNotNull(saleUpdated);
        Assertions.assertNull(saleUpdated.customer());
    }

    private ProductSoldDto createProductSoldDto(ProductDto product, int quantity) {
        return ProductSoldCreator.createDto(product.id(), quantity);
    }

    private SaleDto createSaleDto(CustomerDto customer, double price, List<ProductSoldDto> productsSoldDto, List<InstallmentDto> installmentsDto) {
        return SaleCreator.createDto(
                customer, price, productsSoldDto, installmentsDto
        );
    }

    private CustomerDto createCustomer(String name, String cpf, String nickname, String address, String phone) {
        return CustomerCreator.createDto(
                name, cpf, nickname, address, phone
        );
    }

    private InstallmentDto createInstallment(String dueDate, double price, String paymentMethod, boolean isPaid) {
        return InstallmentCreator.createDto(
                dueDate, price, paymentMethod, isPaid
        );
    }

    private CustomerDto saveCustomer(CustomerDto customerDto) {
        var res = restTemplate.postForEntity(CUSTOMERS_URI, customerDto, CustomerDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
        return res.getBody();
    }

    private ProductDto saveProduct(ProductDto productDto) {
        var res = restTemplate.postForEntity(PRODUCTS_URI, productDto, ProductDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
        return res.getBody();
    }

    private SaleDto saveSale(SaleDto saleDto) {
        var res = restTemplate.postForEntity(SALES_URI, saleDto, SaleDto.class);
        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());
        return res.getBody();
    }

    private ProductDto createProductDto(String name, double price, int stockQuantity) {
        return ProductCreator.createDto(name, price, stockQuantity);
    }

    private void deleteProduct(ProductDto productDto) {
        restTemplate.delete(PRODUCTS_URI + "/" + productDto.id());
    }

    private ProductSoldDto getProductSold(ProductSoldDto productSoldDto) {
        var productSold = productSoldRepository.findById(productSoldDto.id()).orElse(null);
        return ProductSoldMapper.toDto(productSold);
    }

    private void deleteCustomer(CustomerDto customerDto) {
        restTemplate.delete(CUSTOMERS_URI + "/" + customerDto.id());
    }

    private SaleDto findSaleById(SaleDto saleDto) {
        var res = restTemplate.getForEntity(
                SALES_URI + "/" + saleDto.id(),
                SaleDto.class
        );

        return res.getBody();
    }
}


