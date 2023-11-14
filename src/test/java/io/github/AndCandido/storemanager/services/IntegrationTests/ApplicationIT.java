package io.github.AndCandido.storemanager.services.IntegrationTests;

import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.CustomerModel;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.ICustomerRepository;
import io.github.AndCandido.storemanager.domain.repositories.IProductRepository;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import io.github.AndCandido.storemanager.services.utils.CustomerCreator;
import io.github.AndCandido.storemanager.services.utils.ProductCreator;
import io.github.AndCandido.storemanager.services.utils.ProductSoldCreator;
import io.github.AndCandido.storemanager.services.utils.SaleCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ApplicationIT {

    @Autowired
    private ISaleRepository saleRepository;
    @Autowired
    private ISaleService saleService;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductSoldRepository productSoldRepository;
    @Autowired
    private ICustomerRepository customerRepository;

    private List<ProductModel> productModels;
    private CustomerModel customerModel;
    private SaleModel saleModel;
    private List<ProductSoldModel> productSoldModels;

    @BeforeEach
    void beforeAll() {
        productModels = List.of(
                createProductModel("Meia", 18.99, 5),
                createProductModel("Calça", 145.00, 1),
                createProductModel("Camisa", 45.50, 7),
                createProductModel("Blusa", 45.05, 21),
                createProductModel("Saia", 122, 50)
        );

        productModels = productRepository.saveAll(productModels);

        var productSoldDtos = List.of(
                createProductSoldDto(productModels.get(0), 4),
                createProductSoldDto(productModels.get(1), 1),
                createProductSoldDto(productModels.get(2), 4)
        );

        productModels = productRepository.findAll();

        customerModel = customerRepository.save(createCustomerModel());
        saleModel = saleService.saveSale(createSaleDto(productSoldDtos));
        productSoldModels = saleModel.getProductsSold();
        customerModel.setSaleModels(List.of(saleModel));
    }

    @Test
    void deleteTwoProducts() {
        List<UUID> productsSoldOfProductsDeletedIds = List.of(
                productSoldRepository.findByProductModel(productModels.get(0)).get(0).getId(),
                productSoldRepository.findByProductModel(productModels.get(1)).get(0).getId()
        );

        productRepository.delete(productModels.get(0));
        productRepository.delete(productModels.get(1));

        for (UUID id : productsSoldOfProductsDeletedIds) {
            var productSoldModel = productSoldRepository.findById(id).orElse(null);
            Assertions.assertNull(productSoldModel.getProductModel());
        }
    }

    @Test
    void deleteAllProduct() {
        productRepository.deleteAll(productModels);
        var productSoldModels = productSoldRepository.findAll();

        for (ProductSoldModel productSoldModel : productSoldModels) {
            Assertions.assertNull(productSoldModel.getProductModel());
        }
    }

    @Test
    void deleteCustomer() {
        customerRepository.delete(customerModel);
        var saleUpdated = saleRepository.findById(saleModel.getId()).orElse(null);

        Assertions.assertNotNull(saleUpdated);
        Assertions.assertNull(saleUpdated.getCustomer());
    }


    private ProductSoldDto createProductSoldDto(ProductModel productModel, int quantity) {
        return ProductSoldCreator.createDto(productModel.getId(), quantity);
    }

    private SaleDto createSaleDto(List<ProductSoldDto> productSoldDtos) {
        return SaleCreator.createDto(
                (short) 1,"PIX", customerModel, 183.88, productSoldDtos
        );
    }

    private CustomerModel createCustomerModel() {
        return CustomerCreator.createModel(
                "Alberto Lima Castro", "88888888888", null, "Rua. ", null
        );
    }

    private ProductModel createProductModel(String name, double price, int stockQuantity) {
        return ProductCreator.createModel(name, price, stockQuantity);
    }
}


