package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.SaleResponseDto;
import io.github.AndCandido.storemanager.services.creators.SaleCreator;

import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SaleDataTest {

    public static final String SALES_URI = "/sales";

    @Getter
    private List<SaleRequestDto> salesRequestDto;
    @Getter@Setter
    private List<SaleResponseDto> salesSaved;
    private final ISaleRepository saleRepository;

    public SaleDataTest(ISaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public SaleDataTest createSales(
        UUID customerId,
        List<ProductSoldRequestDto> productsSoldRequestDto,
        List<InstallmentRequestDto> installmentsRequestDto
    ) {
        salesRequestDto = List.of(
            SaleCreator.createSaleRequestDto(customerId, 200, productsSoldRequestDto, installmentsRequestDto)
        );
        return this;
    }

    public SaleRequestDto getSaleDto(int index) {
        return salesRequestDto.get(index);
    }

    public SaleResponseDto getSaleSaved(int index) {
        return salesSaved.get(index);
    }

    public void cleanDataBase() {
        saleRepository.deleteAll();
    }
}
