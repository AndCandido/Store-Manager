package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.mappers.SaleMapper;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.services.utils.CustomerCreator;
import io.github.AndCandido.storemanager.services.utils.ProductCreator;
import io.github.AndCandido.storemanager.services.utils.ProductSoldCreator;
import io.github.AndCandido.storemanager.services.utils.SaleCreator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaleIT {

    private final String URI = "/sales";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ISaleRepository saleRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICustomerRepository customerRepository;

    private List<ProductModel> productsOriginal;
    private List<ProductSoldDto> productsSoldDtoOriginal;
    private CustomerModel customerOriginal;
    private SaleModel saleSaved;
    private SaleDto saleDto;

    @BeforeEach
    void beforeEach() {
        productsOriginal = List.of(
                createProduct("Meia Social", 19.99, 18),
                createProduct("Calça social", 185.05, 4),
                createProduct("Blusa Juvenil Tam P", 45.50, 10),
                createProduct("Camisa Golo Polo", 90, 11),
                createProduct("short Tec Teu", 18.0, 1)
        );
        productsOriginal = productRepository.saveAll(productsOriginal);

        productsSoldDtoOriginal = List.of(
                createProductSoldDto(productsOriginal.get(0), 18),
                createProductSoldDto(productsOriginal.get(1), 1),
                createProductSoldDto(productsOriginal.get(2), 5),
                createProductSoldDto(productsOriginal.get(3), 8)
        );

        customerOriginal = customerRepository.save(createCustomer());
        saleDto = createSaleDto();
    }

    @AfterEach
    void cleanAll() {
        productRepository.deleteAll();
        customerRepository.deleteAll();
        saleRepository.deleteAll();
    }

    @Test
    public void saveSale_Success() {
        var response = restTemplate.postForEntity(URI, saleDto, SaleDto.class);

        var body = response.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertionsNotNullBodySale(body);
        assertionsBodySale(body);
    }

    @Test
    public void getAllSales_Success() {
        saleSaved = saveSaleDto();

        var res = restTemplate.exchange(
                URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SaleDto>>() {}
        );

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        var body = res.getBody();
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isEmpty());
        for (SaleDto saleResDto : body) {
            Assertions.assertNotNull(saleResDto);
        }
    }

    @Test
    public void getSaleById_Success() {
        saleSaved = saveSaleDto();

        var res = restTemplate.getForEntity(
                URI + "/" + saleSaved.getId(),
                SaleDto.class
        );
        var body = res.getBody();

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assertions.assertNotNull(body);
    }

    @Test
    public void updateSale_Success() {
        saleSaved = saveSaleDto();

        var productsSoldDtoForUpdate = List.of(
                createProductSoldDto(productsOriginal.get(0), 18),
                createProductSoldDto(productsOriginal.get(1), 3),
                createProductSoldDto(productsOriginal.get(3), 8),
                createProductSoldDto(productsOriginal.get(4), 1)
        );

        saleDto = createSaleDtoForUpdate(productsSoldDtoForUpdate);

        var res =  restTemplate.exchange(
                URI + "/" + saleSaved.getId(),
                HttpMethod.PUT,
                new HttpEntity<SaleDto>(saleDto),
                SaleDto.class
        );

        var body = res.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        assertionsNotNullBodySale(body);
        Assertions.assertEquals(saleSaved.getId(), body.id());
        Assertions.assertEquals(saleSaved.getCreatedAt(), body.createdAt());
        Assertions.assertEquals(productsSoldDtoForUpdate.size(), body.productsSold().size());

        var productsUpdated = productRepository.findAll();
        var productsSoldUpdated = body.productsSold();

        for (ProductModel product : productsOriginal) {
            var productOriginal = getProductOriginalOn(product);
            var productUpdated = getProductUpdatedOn(product, productsUpdated);
            var productSoldOriginal = getProductSoldOriginal(product);
            var productSoldUpdated = getProductSoldUpdated(product, productsSoldUpdated);

            if(productSoldOriginal == null && productSoldUpdated == null) {
                continue;
            }

            if(productSoldOriginal != null) {
                if(productSoldUpdated != null) {
                    assertionsVerifyProductOriginalAndUpdated(productOriginal, productUpdated, productSoldUpdated);
                    continue;
                }

                assertionsPresentOnOriginal(productOriginal, productUpdated);
                continue;
            }

            assertionsVerifyProductOriginalAndUpdated(productOriginal, productUpdated, productSoldUpdated);
        }
    }

    @Test
    public void deleteSale_Success() {
        saleSaved = saveSaleDto();

        var res = restTemplate.exchange(
                URI + "/" + saleSaved.getId(),
                HttpMethod.DELETE,
                null,
                Void.class
        );

        var saleDeleted = saleRepository.findById(saleSaved.getId()).orElse(null);
        Assertions.assertNull(saleDeleted);

        Assertions.assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());

        for (ProductSoldDto productSoldDto : productsSoldDtoOriginal) {
            var productId = productSoldDto.productId();
            var productOriginal = getProductById(productId);
            var productUpdated = getProductUpdatedById(productId);

            Assertions.assertEquals(productOriginal.getStockQuantity(), productUpdated.getStockQuantity());
        }
    }

    private ProductModel getProductUpdatedById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    private ProductModel getProductById(Long id) {
        for (ProductModel productModel : productsOriginal) {
            if(Objects.equals(productModel.getId(), id))
                return productModel;
        }

        return null;
    }

    private void assertionsPresentOnOriginal(
            ProductModel productOriginal,
            ProductModel productUpdated
    ) {
        Assertions.assertEquals(
                productOriginal.getStockQuantity(),
                productUpdated.getStockQuantity()
        );
    }

    private void assertionsVerifyProductOriginalAndUpdated(
            ProductModel productOriginal,
            ProductModel productUpdated,
            ProductSoldDto productSoldDtoUpdated

    ) {
        Assertions.assertEquals(
                productOriginal.getStockQuantity() - productSoldDtoUpdated.quantity(),
                productUpdated.getStockQuantity()
        );
    }

    private void assertionsNotNullBodySale(SaleDto body) {
        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.id());
        Assertions.assertNotNull(body.createdAt());
        Assertions.assertNotNull(body.productsSold());
    }

    private void assertionsBodySale(SaleDto body) {
        var productsUpdated = productRepository.findAll();
        var productsOnSold = new ArrayList<ProductModel>();

        for (ProductModel product : productsOriginal) {


        }
    }

    private SaleModel saveSaleDto() {
        return saleRepository.save(SaleMapper.toModel(saleDto));
    }

    private ProductModel createProduct(String name, double price, int stockQuantity) {
        return ProductCreator.createModel(name, price, stockQuantity);
    }

    private ProductSoldDto createProductSoldDto(ProductModel productModel, int quantity) {
        return ProductSoldCreator.createDto(productModel.getId(), quantity);
    }

    private CustomerModel createCustomer() {
        return CustomerCreator.createModel(
                "Carlos Henrique Lima", "440.052.520-10", "Irmão do Dr.", "Rua. Bairro.", null
        );
    }

    private SaleDto createSaleDto() {
        return SaleCreator.createDto((short) 1, "PIX", customerOriginal, 123.00, productsSoldDtoOriginal);
    }

    private SaleDto createSaleDtoForUpdate(List<ProductSoldDto> productsSoldDtoForUpdate) {
        return SaleCreator.createDto((short) 5, "DEBIT_CARD", customerOriginal, 1000.00, productsSoldDtoForUpdate);
    }

    private ProductModel getProductOriginalOn(ProductModel productModel) {
        return getProductOn(productModel, productsOriginal);
    }

    private ProductModel getProductUpdatedOn(ProductModel productModel, List<ProductModel> productsUpdated) {
        return getProductOn(productModel, productsUpdated);
    }

    private ProductModel getProductOn(ProductModel productModel, List<ProductModel> productsForIterate) {
        for (ProductModel product : productsForIterate) {
            if(Objects.equals(product.getId(), productModel.getId()))
                return product;
        }

        return null;
    }

    private ProductSoldDto getProductSoldOriginal(ProductModel productModel) {
        return getProductSoldOn(productModel, productsSoldDtoOriginal);
    }

    private ProductSoldDto getProductSoldUpdated(ProductModel productModel, List<ProductSoldDto> productsSoldDtoUpdated) {
        return getProductSoldOn(productModel, productsSoldDtoUpdated);
    }

    private ProductSoldDto getProductSoldOn(ProductModel productModel, List<ProductSoldDto> productsSoldDtoForIterate) {
        for (ProductSoldDto productSoldDto : productsSoldDtoForIterate){
            if(Objects.equals(productSoldDto.productId(), productModel.getId())) {
                return productSoldDto;
            }
        }

        return null;
    }
}