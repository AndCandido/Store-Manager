package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.services.dataTest.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ApplicationIT {

    private final ProductDataTest productData;
    private final ProductSoldDataTest productSoldData;
    private final CustomerDataTest customerData;
    private final SaleDataTest saleData;

    @Autowired
    public ApplicationIT(ProductDataTest productData, ProductSoldDataTest productSoldData, CustomerDataTest customerData, InstallmentDataTest installmentData, SaleDataTest saleData) {
         this.productData = productData
            .createProducts().saveProducts();

        this.customerData = customerData
            .createCustomers().saveCustomers();

        this.productSoldData = productSoldData
            .createProductsSold(productData.getProductsDto());

        installmentData
            .createInstallments();

        this.saleData = saleData
            .createSales(
                this.productSoldData.getProductsSoldDto(),
                this.customerData.getCustomersDto(),
                installmentData.getInstallmentsDto()
            )
            .saveSales();

        this.productSoldData.refreshData();

        System.out.println(productData.getProductsDto().size());
    }

    @AfterEach
    void cleanDataBase() {
        productData.cleanDataBase();
        customerData.cleanDataBase();
        saleData.cleanDataBase();
    }

    @Test
    void deleteTwoProducts_ProductIdOnProductSoldIsNull_Success() {
        System.out.println(productData.getProductsDto().size());
        productData.deleteProduct(productData.getProductDto(0));
        productData.deleteProduct(productData.getProductDto(1));

        var productsSoldWhereProductsHasDeleted = List.of(
                productSoldData.findProductSold(productSoldData.getProductSoldDto(0)),
                productSoldData.findProductSold(productSoldData.getProductSoldDto(1))
        );

        for (ProductSoldDto productSoldDto : productsSoldWhereProductsHasDeleted) {
            Assertions.assertNull(productSoldDto.productId());
        }
    }

    @Test
    void deleteCustomer_CustomerIdOnSaleIsNull_Success() {
        customerData.deleteCustomer(customerData.getCustomerDto(0));
        ResponseEntity<SaleDto> response = saleData.findSaleById(saleData.getSaleDto(0));
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        SaleDto saleUpdated = response.getBody();

        Assertions.assertNotNull(saleUpdated);
        Assertions.assertNull(saleUpdated.customer());
    }

    @Test
    void deleteSale_SaleIsNullOnProductAndCustomer_Success() {
        System.out.println(productData.getProductsDto().size());
        saleData.deleteSale(saleData.getSaleDto(0));

        List<ProductDto> productsUpdated = productData
            .findProductsByProductSold(productSoldData.getProductsSoldDto()).stream()
            .map(response -> {
                Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
                return response.getBody();
            })
            .toList();

        for (ProductDto productDto : productsUpdated) {
            System.out.println(productDto);
            Assertions.assertNull(productDto.productsSold());
        }

        ResponseEntity<CustomerDto> responseCustomersUpdated = customerData.findCustomer(customerData.getCustomerDto(0));
        Assertions.assertEquals(HttpStatus.OK, responseCustomersUpdated.getStatusCode());
        CustomerDto customerUpdated = responseCustomersUpdated.getBody();

        assert customerUpdated != null;
        Assertions.assertNull(customerUpdated.sales());
        Assertions.assertNull(customerUpdated.installments());
    }
}