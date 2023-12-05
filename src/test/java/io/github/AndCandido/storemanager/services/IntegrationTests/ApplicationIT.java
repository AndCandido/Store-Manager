package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.*;
import io.github.AndCandido.storemanager.domain.mappers.ProductSoldMapper;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
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
@AutoConfigureTestDatabase
public class ApplicationIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICustomerRepository customerRepository;
    @Autowired
    private IProductSoldRepository productSoldRepository;
    @Autowired
    private ISaleRepository saleRepository;


    private final String PRODUCTS_URI = "/products";
    private final String CUSTOMERS_URI = "/customers";
    private final String SALES_URI = "/sales";

    private List<ProductDto> products = new ArrayList<>();
    private List<CustomerDto> customers = new ArrayList<>();
    private List<ProductSoldDto> productsSoldDto = new ArrayList<>();
    private List<InstallmentDto> installmentsDto = new ArrayList<>();
    private List<SaleDto> sales = new ArrayList<>();

    @BeforeEach
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
                createProductSoldDto(products.get(3), 4)
        ));

        var tomorrowDate = LocalDate.now().plusDays(1);

        installmentsDto.addAll(List.of(
                createInstallment(tomorrowDate.toString(), 30, "DEBIT_CARD", true),
                createInstallment(tomorrowDate.toString(), 40, "NONE", false)
        ));

        customers.addAll(List.of(
                saveCustomer(createCustomer("Alberto Lima Castro", "021.419.780-84", null, "Rua. ", null)),
                saveCustomer(createCustomer("Augusto Ribeiro Arcanjo ", "021.419.780-84", null, "Rua. ", null)),
                saveCustomer(createCustomer("Júlio Antônia Cardoso Castro", "021.419.780-84", "Comercio do Júlio", "Rua. ", null))
        ));

        sales.add(
                saveSale(createSaleDto(customers.get(0), 200.99, productsSoldDto, installmentsDto))
        );

        productsSoldDto = productSoldRepository.findAll().stream().map(ProductSoldMapper::toDto).toList();
    }

    @AfterEach
    void cleanDataBase() {
        productRepository.deleteAll();
        customerRepository.deleteAll();
        saleRepository.deleteAll();
    }

    @Test
    void deleteTwoProducts_ProductIdOnProductSoldIsNull_Success() {
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

    @Test
    void deleteCustomer_CustomerIdOnSaleIsNull_Success() {
        deleteCustomer(customers.get(0));
        var saleUpdated = findSaleById(sales.get(0));

        Assertions.assertNotNull(saleUpdated);
        Assertions.assertNull(saleUpdated.customer());
    }

    @Test
    void deleteSale_SaleIsNullOnProductAndCustomer_Success() {
        deleteSale(sales.get(0));

        var productsUpdated = getProductsByProductSold(productsSoldDto);
        var customersUpdated = getCustomers(customers.get(0));

        for (ProductDto productDto : productsUpdated) {
            System.out.println(productDto);
            Assertions.assertNull(productDto.productsSold());
        }

        Assertions.assertNull(customersUpdated.sales());
        Assertions.assertNull(customersUpdated.installments());
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
        assert productSold != null;
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

    private void deleteSale(SaleDto saleDto) {
        restTemplate.delete(SALES_URI + "/" + saleDto.id());
    }

    private List<ProductDto> getProductsByProductSold(List<ProductSoldDto> productsSoldDto) {
        var products = new ArrayList<ProductDto>(productsSoldDto.size());

        for (ProductSoldDto productSoldDto : productsSoldDto) {
            var res = restTemplate.getForEntity(PRODUCTS_URI + "/" + productSoldDto.productId(), ProductDto.class);
            products.add(res.getBody());
        }

        return products;
    }

    private CustomerDto getCustomers(CustomerDto customerDto) {
        return restTemplate.getForEntity(CUSTOMERS_URI + "/" + customerDto, CustomerDto.class).getBody();
    }
}


