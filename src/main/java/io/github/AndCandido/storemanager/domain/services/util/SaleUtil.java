package io.github.AndCandido.storemanager.domain.services.util;

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

import java.util.ArrayList;
import java.util.List;

@Component
public class SaleUtil {

    private IProductSoldRepository productSoldRepository;
    private ICustomerService customerService;
    private IProductService productService;

    SaleUtil(IProductSoldRepository productSoldRepository,
             ICustomerService customerService,
             IProductService productService
    ) {
        this.productSoldRepository = productSoldRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    public void handlerSetSaleModel(SaleDto saleDto, SaleModel saleModel) {
        BeanUtils.copyProperties(saleDto, saleModel);

        for (int i = 0; i < saleDto.productsSold().size(); i++) {
            var productSoldDto = saleDto.productsSold().get(i);
            var productSoldModel = new ProductSoldModel();
            setModelProperties(productSoldModel, productSoldDto);
            saleModel.getProductsSold().add(productSoldModel);
        }

        for(ProductSoldModel productSoldModel : saleModel.getProductsSold()) {
            updateProductStockQuantity(productSoldModel);
        }

        productSoldRepository.saveAll(saleModel.getProductsSold());
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

    public void handlerUpdateSaleModel(SaleDto saleDto, SaleModel saleModel) {

    }
}
