package io.github.AndCandido.storemanager.utils;

import io.github.AndCandido.storemanager.api.exceptions.InsufficientStockException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.domain.services.ICustomerService;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaleUtil {
    private IProductSoldRepository productSoldRepository;
    private ICustomerService customerService;
    private IProductService productService;

    public SaleUtil(IProductSoldRepository productSoldRepository,
             ICustomerService customerService,
             IProductService productService
    ) {
        this.productSoldRepository = productSoldRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    public void handlerSetSaleModel(SaleDto saleDto, SaleModel saleModel) {
        BeanUtils.copyProperties(saleDto, saleModel);

        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var productSoldModel = new ProductSoldModel();
            setModelProperties(productSoldModel, productSoldDto);
            saleModel.getProductsSold().add(productSoldModel);
        }

        for(ProductSoldModel productSoldModel : saleModel.getProductsSold()) {
            updateProductStockQuantity(productSoldModel);
        }

        productSoldRepository.saveAll(saleModel.getProductsSold());
    }

    public void handlerUpdateSaleModel(SaleDto saleDto, SaleModel saleModel) {
        BeanUtils.copyProperties(saleDto, saleModel);

        Map<Long, ProductModel> originalProducts = new HashMap<>();

        saleModel.getProductsSold().forEach(productSoldModel -> {
            var product = productSoldModel.getProductModel();
            var stockQuantity = product.getStockQuantity();
            var quantitySold = productSoldModel.getQuantity();

            product.setStockQuantity(stockQuantity + quantitySold);
            originalProducts.put(product.getId(), product);
        });

        saleModel.getProductsSold().clear();

        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var productSoldModel = new ProductSoldModel();

            setModelProperties(productSoldModel, productSoldDto);
            saleModel.getProductsSold().add(productSoldModel);
        }

        for (ProductSoldModel productSoldModel : saleModel.getProductsSold()) {
            var product = productSoldModel.getProductModel();

            if(originalProducts.containsKey(product.getId())) {
                productSoldModel.setProductModel(originalProducts.get(product.getId()));
            }

            updateProductStockQuantity(productSoldModel);
        }

        productSoldRepository.saveAll(saleModel.getProductsSold());

        saleModel.getProductsSold().forEach(productSoldModel -> {
            var product = productSoldModel.getProductModel();
            productService.updateProduct(product);
        });
    }

    private void setModelProperties(
            ProductSoldModel productSoldModel, ProductSoldDto productSoldDto
    ) {
        BeanUtils.copyProperties(productSoldDto, productSoldModel);
        ProductModel product = getProduct(productSoldDto.productId());
        productSoldModel.setProductModel(product);
    }


    private ProductModel getProduct(Long productId) {
        return productService.getProductById(productId);
    }

    private void updateProductStockQuantity(ProductSoldModel productSoldModel) {
        int stockQuantityRemaining = getStockQuantityRemaining(productSoldModel);
        productSoldModel.getProductModel().setStockQuantity(stockQuantityRemaining);
    }

    private int getStockQuantityRemaining(ProductSoldModel productSoldModel) {
        int stockQuantity = productSoldModel.getProductModel().getStockQuantity();
        int quantitySold = productSoldModel.getQuantity();
        String productName = productSoldModel.getProductModel().getName();

        verifyHasSufficientStockQuantity(stockQuantity, quantitySold, productName);

        return stockQuantity - quantitySold;
    }

    private void verifyHasSufficientStockQuantity(int stockQuantity, int quantitySold, String productName) {
        if(stockQuantity - quantitySold < 0) {
            throw new InsufficientStockException(
                    "Não há quantidade em estoque o suficiente de "+productName+", há somente "+ stockQuantity + " unidade(s)"
            );
        }
    }
}