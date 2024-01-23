package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductSoldResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.ProductSoldMapper;
import io.github.AndCandido.storemanager.domain.repositories.IProductSoldRepository;
import io.github.AndCandido.storemanager.services.creators.ProductSoldCreator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductSoldDataTest {

    @Getter
    private List<ProductSoldRequestDto> productsSoldRequestDto;
    @Getter@Setter
    private List<ProductSoldResponseDto> productsSoldSaved;
    private final IProductSoldRepository productSoldRepository;

    public ProductSoldDataTest(IProductSoldRepository productSoldRepository) {
        this.productSoldRepository = productSoldRepository;
    }

    public ProductSoldDataTest createRequestProductsSold(List<ProductResponseDto> productsResponseDto) {
        productsSoldRequestDto = List.of(
            ProductSoldCreator.createProductSoldRequestDto(productsResponseDto.get(0).id(), 4),
            ProductSoldCreator.createProductSoldRequestDto(productsResponseDto.get(1).id(), 1),
            ProductSoldCreator.createProductSoldRequestDto(productsResponseDto.get(2).id(), 4)
        );

        return this;
    }

    public ProductSoldRequestDto getProductSoldDto(int index) {
        return productsSoldRequestDto.get(index);
    }
    public ProductSoldResponseDto getProductSoldSaved(int index) {
        return productsSoldSaved.get(index);
    }

    public void refreshData() {
        productsSoldRequestDto = productSoldRepository.findAll().stream().map(ProductSoldMapper::toRequestDto).toList();
    }
}
