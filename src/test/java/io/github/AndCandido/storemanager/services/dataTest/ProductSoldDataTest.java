package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.mappers.ProductSoldMapper;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import static io.github.AndCandido.storemanager.services.creators.ProductSoldCreator.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductSoldDataTest {

    @Getter
    @Setter
    private List<ProductSoldDto> productsSoldDto;
    private final IProductSoldRepository productSoldRepository;

    public ProductSoldDataTest(IProductSoldRepository productSoldRepository) {
        this.productSoldRepository = productSoldRepository;
    }

    public ProductSoldDataTest createProductsSold(List<ProductDto> productsDto) {
        productsSoldDto = List.of(
            createProductSoldDto(productsDto.get(0).id(), 4),
            createProductSoldDto(productsDto.get(1).id(), 1),
            createProductSoldDto(productsDto.get(2).id(), 4)
        );

        return this;
    }

    public ProductSoldDto findProductSold(ProductSoldDto productSoldDto) {
        var productSold = productSoldRepository.findById(productSoldDto.id()).orElse(null);
        assert productSold != null;
        return ProductSoldMapper.toDto(productSold);
    }

    public ProductSoldDto getProductSoldDto(int i) {
        return productsSoldDto.get(i);
    }

    public void refreshData() {
        productsSoldDto = productSoldRepository.findAll().stream().map(ProductSoldMapper::toDto).toList();
    }
}
