package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.*;
import io.github.AndCandido.storemanager.services.creators.*;
import io.github.AndCandido.storemanager.services.dataTest.ProductDataTest;
import io.github.AndCandido.storemanager.services.dataTest.CustomerDataTest;
import io.github.AndCandido.storemanager.services.dataTest.InstallmentDataTest;
import io.github.AndCandido.storemanager.services.dataTest.ProductSoldDataTest;
import io.github.AndCandido.storemanager.services.dataTest.SaleDataTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class SaleIT {

    private final ProductDataTest productData;
    private final CustomerDataTest customerData;
    private final ProductSoldDataTest productSoldData;
    private final InstallmentDataTest installmentData;
    private final SaleDataTest saleData;

    @Autowired
    public SaleIT(ProductDataTest productData, CustomerDataTest customerData, ProductSoldDataTest productSoldData, InstallmentDataTest installmentData, SaleDataTest saleData) {
        this.productData = productData.createProducts().saveProducts();
        this.productSoldData = productSoldData.createProductsSold(productData.getProductsDto());
        this.customerData = customerData.createCustomers().saveCustomers();
        this.installmentData = installmentData.createInstallments();
        this.saleData = saleData.createSales(productSoldData.getProductsSoldDto(), customerData.getCustomersDto(), installmentData.getInstallmentsDto());
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
        customerData.cleanDataBase();
        saleData.cleanDataBase();
    }

    @Test
    public void saveSale_Success() {
        saleData.saveSales();

        assertionsNotNullBodySaleList(saleData.getSalesDto());
        assertionsBodySaleList(saleData.getSalesDto());
    }

    @Test
    public void getAllSales_Success() {
        saleData.saveSales();

        ResponseEntity<List<SaleDto>> res = saleData.findAllSales();

        List<SaleDto> body = res.getBody();

        Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());

        assertionsNotNullBodySaleList(body);
        assertionsBodySaleList(body);
    }

    @Test
    public void getSaleById_Success() {
        saleData.saveSales();

        for (SaleDto saleDto : saleData.getSalesDto()) {
            ResponseEntity<SaleDto> res = saleData.findSaleById(saleDto);

            Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
            var body = res.getBody();

            assertionsNotNullBodySale(body);
            assertionsBodySale(body);
        }
    }

    @Test
    public void updateSale_Success() {
        saleData.saveSales();
        SaleDto saleSaved = saleData.getSaleDto(0);

        List<ProductSoldDto> productsSoldDtoForUpdate = List.of(
            createProductSoldDto(productData.getProductDto(0), productData.getProductDto(0).stockQuantity() - 1),
            createProductSoldDto(productData.getProductDto(1), productData.getProductDto(1).stockQuantity()),
            createProductSoldDto(productData.getProductDto(3), productData.getProductDto(3).stockQuantity() - 1),
            createProductSoldDto(productData.getProductDto(4), productData.getProductDto(4).stockQuantity())
        );

        SaleDto saleDtoForUpdate = createSaleDtoForUpdate(
            productsSoldDtoForUpdate,
            customerData.getCustomerDto(0),
            installmentData.getInstallmentsDto()
        );

        ResponseEntity<SaleDto> res = saleData.updateSale(saleSaved.id(), saleDtoForUpdate);

        var body = res.getBody();

        Assertions.assertEquals(HttpStatus.CREATED, res.getStatusCode());

        assertionsNotNullBodySale(body);
        Assertions.assertEquals(saleSaved.id(), body.id());
        Assertions.assertEquals(saleSaved.createdAt(), body.createdAt());
        Assertions.assertEquals(productsSoldDtoForUpdate.size(), body.productsSold().size());

        ResponseEntity<List<ProductDto>> productsUpdated = productData.findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, productsUpdated.getStatusCode());

        HashMap<Long, ProductDto> productsMappedOriginal = new HashMap<>();
        productData.getProductsDto().forEach(productDto -> {
            productsMappedOriginal.put(productDto.id(), productDto);
        });

        var productsSoldUpdated = body.productsSold();

        for (ProductDto productDto : productsMappedOriginal.values()) {
            ProductDto productDtoOriginal = productsMappedOriginal.get(productDto.id());
            ProductDto productUpdated = getProductUpdatedOn(productDto, productsUpdated.getBody());
            ProductSoldDto productSoldOriginal = getProductSoldOriginal(productDto);
            ProductSoldDto productSoldUpdated = getProductSoldUpdated(productDto, productsSoldUpdated);

            if (productSoldOriginal == null && productSoldUpdated == null) {
                continue;
            }

            if (productSoldOriginal != null) {
                if (productSoldUpdated != null) {
                    assertionsVerifyProductOriginalAndUpdated(productDtoOriginal, productUpdated, productSoldUpdated);
                    continue;
                }

                assertionsPresentOnOriginal(productDtoOriginal, productUpdated);
                continue;
            }

            assertionsVerifyProductOriginalAndUpdated(productDtoOriginal, productUpdated, productSoldUpdated);
        }
    }

    @Test
    public void deleteSale_Success() {
        saleData.saveSales();

        ResponseEntity<Void> res = saleData.deleteSale(saleData.getSaleDto(0));
        Assertions.assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());

        ResponseEntity<SaleDto> responseSaleDeleted = saleData.findSaleById(saleData.getSaleDto(0));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaleDeleted.getStatusCode());

        for (ProductSoldDto productSoldDto : productSoldData.getProductsSoldDto()) {
            Long productId = productSoldDto.productId();
            ProductDto productOriginal = productData.getProductsDto().stream()
                .filter(productDto -> Objects.equals(productId, productDto.id()))
                .findFirst()
                .orElse(null);

            ResponseEntity<ProductDto> productUpdated = productData.findProductById(productId);
            Assertions.assertEquals(HttpStatus.OK, productUpdated.getStatusCode());

            assert productOriginal != null;
            Assertions.assertEquals(productOriginal.stockQuantity(), productUpdated.getBody().stockQuantity());
        }
    }


    private void assertionsBodySale(SaleDto body) {
        ResponseEntity<List<ProductDto>> responseGetAllProducts = productData.findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, responseGetAllProducts.getStatusCode());

        List<ProductDto> productsDto = responseGetAllProducts.getBody();
        Assertions.assertNotNull(productsDto);

        HashMap<Long, ProductDto> productsMappedUpdated = new HashMap<>();

        instanceProductsMapped(productsMappedUpdated, productsDto);

        assertionsProductSold(body, productsMappedUpdated);
        assertionsInstallments(body);
    }

    private void assertionsBodySaleList(List<SaleDto> body) {
        for (SaleDto saleBody : body) {
            assertionsBodySale(saleBody);
        }
    }

    private void instanceProductsMapped(HashMap<Long, ProductDto> productsMapped, List<ProductDto> productsDto) {
        for (ProductDto productDto : productsDto) {
            productsMapped.put(productDto.id(), productDto);
        }
    }

    private void assertionsProductSold(SaleDto body, HashMap<Long, ProductDto> productsMappedUpdated) {
        HashMap<Long, ProductDto> productsMappedOriginal = new HashMap<>();
        productData.getProductsDto().forEach(productDto -> {
            productsMappedOriginal.put(productDto.id(), productDto);
        });

        for (ProductSoldDto productSoldDto : body.productsSold()) {
            var productId = productSoldDto.productId();
            Assertions.assertNotNull(productId);

            var productOriginal = productsMappedOriginal.get(productId);
            var productUpdated = productsMappedUpdated.get(productId);

            var stockQuantityOriginal = productOriginal.stockQuantity();
            var stockQuantityUpdated = productUpdated.stockQuantity();

            Assertions.assertEquals(stockQuantityOriginal - productSoldDto.quantity(), stockQuantityUpdated);
        }
    }

    private void assertionsInstallments(SaleDto body) {
        for (InstallmentDto installment : body.installments()) {
            Assertions.assertNotNull(installment);
        }
    }

    private void assertionsNotNullBodySale(SaleDto body) {
        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.id());
        Assertions.assertNotNull(body.price());
        Assertions.assertNotNull(body.customer());
        Assertions.assertNotNull(body.installments());
        Assertions.assertNotNull(body.productsSold());
        Assertions.assertNotNull(body.createdAt());
    }

    private void assertionsNotNullBodySaleList(List<SaleDto> body) {
        Assertions.assertNotNull(body);
        Assertions.assertFalse(body.isEmpty());

        for (SaleDto saleBody : body) {
            assertionsNotNullBodySale(saleBody);
        }
    }

    private ProductSoldDto createProductSoldDto(ProductDto product, int quantity) {
        return ProductSoldCreator.createProductSoldDto(product.id(), quantity);
    }

    private SaleDto createSaleDtoForUpdate(List<ProductSoldDto> productsSoldDtoForUpdate, CustomerDto customerDto, List<InstallmentDto> installmentsDto) {
        return SaleCreator.createSaleDto(customerDto, 1000.00, productsSoldDtoForUpdate, installmentsDto);
    }

    private ProductDto getProductUpdatedOn(ProductDto product, List<ProductDto> productsUpdated) {
        return getProductOn(product, productsUpdated);
    }

    private ProductDto getProductOn(ProductDto productModel, List<ProductDto> productsForIterate) {
        for (ProductDto productDto : productsForIterate) {
            if (Objects.equals(productDto.id(), productModel.id()))
                return productDto;
        }

        return null;
    }

    private ProductSoldDto getProductSoldOriginal(ProductDto productDto) {
        return getProductSoldOn(productDto, productSoldData.getProductsSoldDto());
    }

    private ProductSoldDto getProductSoldOn(ProductDto productDto, List<ProductSoldDto> productsSoldDtoForIterate) {
        for (ProductSoldDto productSoldDto : productsSoldDtoForIterate) {
            if (Objects.equals(productSoldDto.productId(), productDto.id())) {
                return productSoldDto;
            }
        }

        return null;
    }

    private ProductSoldDto getProductSoldUpdated(ProductDto productDto, List<ProductSoldDto> productsSoldDtoUpdated) {
        return getProductSoldOn(productDto, productsSoldDtoUpdated);
    }

    private void assertionsVerifyProductOriginalAndUpdated(
        ProductDto productDtoOriginal,
        ProductDto productDtoUpdated,
        ProductSoldDto productSoldDtoUpdated

    ) {
        Assertions.assertEquals(
            productDtoOriginal.stockQuantity() - productSoldDtoUpdated.quantity(),
            productDtoUpdated.stockQuantity()
        );
    }

    private void assertionsPresentOnOriginal(
        ProductDto productDtoOriginal,
        ProductDto productDtoUpdated
    ) {
        Assertions.assertEquals(
            productDtoOriginal.stockQuantity(),
            productDtoUpdated.stockQuantity()
        );
    }
}