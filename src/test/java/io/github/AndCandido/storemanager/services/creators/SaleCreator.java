package io.github.AndCandido.storemanager.services.creators;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.ProductSoldRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;

import java.util.List;
import java.util.UUID;

public class SaleCreator {

    public static SaleRequestDto createSaleRequestDto(
        UUID customerId,
        double price,
        List<ProductSoldRequestDto> productsSoldRequestDto,
        List<InstallmentRequestDto> installmentsRequestDto
    ) {
        return SaleRequestDto.builder()
            .customerId(customerId)
            .price(price)
            .productsSold(productsSoldRequestDto)
            .installments(installmentsRequestDto)
            .build();
    }
}
