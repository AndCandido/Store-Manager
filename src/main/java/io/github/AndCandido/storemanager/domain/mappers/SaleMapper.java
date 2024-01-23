package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.responses.SaleResponseDto;
import io.github.AndCandido.storemanager.domain.models.Sale;

import java.util.List;

public class SaleMapper {

    public static SaleResponseDto toDto(Sale sale) {
        if(sale == null) return null;

        var customerWithoutAssociations = CustomerMapper.toDtoWithoutAssociations(sale.getCustomer());
        var productsSoldDto = ProductSoldMapper.toDtoList(sale.getProductsSold());
        var installmentsDto = InstallmentMapper.toDtoListWithoutAssociations(sale.getInstallments());

        return SaleResponseDto.builder()
                .id(sale.getId())
                .customer(customerWithoutAssociations)
                .productsSold(productsSoldDto)
                .installments(installmentsDto)
                .price(sale.getPrice())
                .createdAt(sale.getCreatedAt())
                        .build();
    }


    public static SaleResponseDto toDtoWithoutAssociations(Sale sale) {
        if(sale == null) return null;

        return SaleResponseDto.builder()
                .id(sale.getId())
                .price(sale.getPrice())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    public static List<SaleResponseDto> toDtoListWithoutAssociations(List<Sale> sale) {
        return sale == null || sale.isEmpty() ? null :
                sale.stream().map(SaleMapper::toDtoWithoutAssociations).toList();
    }
}
