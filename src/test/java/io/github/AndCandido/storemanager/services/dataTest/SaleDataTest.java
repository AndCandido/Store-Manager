package io.github.AndCandido.storemanager.services.dataTest;

import io.github.AndCandido.storemanager.domain.dtos.CustomerDto;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.ProductSoldDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import static io.github.AndCandido.storemanager.services.creators.SaleCreator.*;

import io.github.AndCandido.storemanager.domain.repositories.ISaleRepository;
import io.github.AndCandido.storemanager.services.restTemplates.ResourceTestRestTemplate;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class SaleDataTest {
    private static final String SALES_URI = "/sales";
    
    @Getter
    private List<SaleDto> salesDto;
    
    private final ResourceTestRestTemplate<SaleDto, UUID> restTemplate;
    private final ISaleRepository saleRepository;

    public SaleDataTest(ResourceTestRestTemplate<SaleDto, UUID> restTemplate, ISaleRepository saleRepository) {
        this.restTemplate = restTemplate;
        this.saleRepository = saleRepository;
    }

    public SaleDataTest createSales(List<ProductSoldDto> productsSoldDto, List<CustomerDto> customersDto, List<InstallmentDto> installmentsDto) {
        salesDto = List.of(
            createSaleDto(customersDto.get(0), 200, productsSoldDto, installmentsDto)
        );
        return this;
    }

    public SaleDataTest saveSales() {
        salesDto = restTemplate
            .postAll(SALES_URI, salesDto, new ParameterizedTypeReference<SaleDto>(){}).stream()
            .map(response -> {
                Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
                return response.getBody();
            })
            .toList();

        return this;
    }

    public SaleDto getSaleDto(int i) {
        return salesDto.get(i);
    }

    public ResponseEntity<List<SaleDto>> findAllSales() {
        return restTemplate.getAll(SALES_URI, new ParameterizedTypeReference<List<SaleDto>>() {});
    }

    public ResponseEntity<SaleDto> findSaleById(SaleDto saleDto) {
        return restTemplate.getById(SALES_URI, saleDto.id(), new ParameterizedTypeReference<>() {});
    }

    public ResponseEntity<Void> deleteSale(SaleDto saleDto) {
        return restTemplate.delete(SALES_URI, saleDto.id());
    }

    public void cleanDataBase() {
        saleRepository.deleteAll();
    }

    public ResponseEntity<SaleDto> updateSale(UUID saleId, SaleDto saleDtoForUpdate) {
        return restTemplate.put(SALES_URI, saleId, saleDtoForUpdate, new ParameterizedTypeReference<SaleDto>() {});
    }
}
