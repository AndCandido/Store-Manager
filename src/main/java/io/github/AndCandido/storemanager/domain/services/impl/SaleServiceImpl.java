package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.InsufficientStockException;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.models.ProductSoldModel;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import io.github.AndCandido.storemanager.domain.services.IProductSoldService;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SaleServiceImpl implements ISaleService {

    private ISaleRepository saleRepository;
    private IProductService productService;
    private IProductSoldService productSoldService;

    public SaleServiceImpl(ISaleRepository saleRepository, IProductService productService, IProductSoldService productSoldService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.productSoldService = productSoldService;
    }

    @Override
    @Transactional
    public SaleModel saveSale(SaleDto saleDto) {
        var saleModel = createSaleModel(saleDto);
        var saleSaved = saleRepository.save(saleModel);

        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var quantitySold = productSoldDto.quantity();
            var productSoldModel = createAndInstanceProductSoldModel(productSoldDto, saleModel);
            var productModel = productSoldModel.getProductModel();

            updateStockQuantityOfProduct(productModel, quantitySold);
            persistProductAndProductSold(productModel, productSoldModel);
        }

        return saleSaved;
    }

    @Override
    public List<SaleModel> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public SaleModel getSaleById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));
    }

    @Override
    @Transactional
    public SaleModel updateSale(SaleDto saleDto, UUID id) {
        var saleModel = getSaleById(id);

        for (ProductSoldModel productSoldModel : saleModel.getProductsSold()) {
            var productModel = productSoldModel.getProductModel();
            incrementStockQuantityOfProduct(productModel, productSoldModel.getQuantity());
            updateProduct(productModel);
            deleteProductSold(productSoldModel);
        }

        saleModel.getProductsSold().clear();

        for (ProductSoldDto productSoldDto : saleDto.productsSold()) {
            var quantitySold = productSoldDto.quantity();
            var productSoldModel = createAndInstanceProductSoldModel(productSoldDto, saleModel);
            var productModel = productSoldModel.getProductModel();

            updateStockQuantityOfProduct(productModel, quantitySold);
            persistProductAndProductSold(productModel, productSoldModel);
        }

        return saleRepository.save(saleModel);
    }

    @Override
    public void deleteSale(UUID id) {
        var saleModel = getSaleById(id);

        for (ProductSoldModel productSoldModel : saleModel.getProductsSold()) {
            var productModel = productSoldModel.getProductModel();
            incrementStockQuantityOfProduct(productModel, productSoldModel.getQuantity());
            updateProduct(productModel);
            deleteProductSold(productSoldModel);
        }

        saleRepository.delete(saleModel);
    }

    private SaleModel createSaleModel(SaleDto saleDto) {
        var saleModel = new SaleModel();
        BeanUtils.copyProperties(saleDto, saleModel);
        return saleModel;
    }

    private ProductSoldModel createAndInstanceProductSoldModel(ProductSoldDto productSoldDto, SaleModel saleModel) {
        var productSoldModel = createProductSoldModel(productSoldDto);
        var productModel = getProductById(productSoldDto.productId());
        setProductModelOnProductSold(productSoldModel, productModel);
        setSaleOnProductSold(productSoldModel, saleModel);

        saleModel.getProductsSold().add(productSoldModel);

        return productSoldModel;
    }

    private void updateStockQuantityOfProduct(ProductModel productModel, int quantitySold) {
        if (!isValidSold(productModel.getStockQuantity(), quantitySold)) {
            throwInsufficientStock(productModel.getName(), productModel.getStockQuantity());
        }

        decrementStockQuantityProduct(productModel, quantitySold);
    }

    private void persistProductAndProductSold(ProductModel productModel, ProductSoldModel productSoldModel) {
        updateProduct(productModel);
        saveProductSold(productSoldModel);
    }

    private ProductSoldModel createProductSoldModel(ProductSoldDto productSoldDto) {
        var productSoldModel = new ProductSoldModel();
        BeanUtils.copyProperties(productSoldDto, productSoldModel);
        return productSoldModel;
    }

    private ProductModel getProductById(Long id) {
        return productService.getProductById(id);
    }

    private boolean isValidSold(Integer stockQuantity, Integer quantitySold) {
        return stockQuantity - quantitySold >= 0;
    }

    private void throwInsufficientStock(String productName, Integer stockQuantity) {
        throw new InsufficientStockException(
                "Estoque insuficiente de " + productName + " para realizar esta compra, há " + stockQuantity + " unidade(s)"
        );
    }

    private void decrementStockQuantityProduct(ProductModel productModel, Integer quantitySold) {
        var stockQuantity = productModel.getStockQuantity();
        productModel.setStockQuantity(stockQuantity - quantitySold);
    }

    private void updateProduct(ProductModel productModel) {
        productService.updateProduct(productModel);
    }

    private void setProductModelOnProductSold(ProductSoldModel productSoldModel, ProductModel productModel) {
        productSoldModel.setProductModel(productModel);
    }

    private void setSaleOnProductSold(ProductSoldModel productSoldModel, SaleModel saleModel) {
        productSoldModel.setSale(saleModel);
    }

    private void saveProductSold(ProductSoldModel productSoldModel) {
        productSoldService.saveProductSold(productSoldModel);
    }

    private void deleteProductSold(ProductSoldModel productSoldModel) {
        productSoldService.deleteProductSold(productSoldModel.getId());
    }

    private void incrementStockQuantityOfProduct(ProductModel productModel, Integer quantitySold) {
        var stockQuantity = productModel.getStockQuantity();
        productModel.setStockQuantity(stockQuantity + quantitySold);
    }
}
