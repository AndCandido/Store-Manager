package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;

import java.util.List;

public class SaleMapper {

    public static SaleDto toDto(Sale sale) {
        if(sale == null) return null;

        var customerWithoutAssociations = CustomerMapper.toDtoWithoutAssociations(sale.getCustomer());
        var productsSoldDto = ProductSoldMapper.toDtoList(sale.getProductsSold());
        var installmentsDto = InstallmentMapper.toDtoListWithoutAssociations(sale.getInstallments());

        return SaleDto.builder()
                .id(sale.getId())
                .customer(customerWithoutAssociations)
                .productsSold(productsSoldDto)
                .installments(installmentsDto)
                .price(sale.getPrice())
                .createdAt(sale.getCreatedAt())
                        .build();
    }


    public static SaleDto toDtoWithoutAssociations(Sale sale) {
        if(sale == null) return null;

        return SaleDto.builder()
                .id(sale.getId())
                .price(sale.getPrice())
                .createdAt(sale.getCreatedAt())
                .build();
    }

    public static List<SaleDto> toDtoListWithoutAssociations(List<Sale> sale) {
        return sale == null || sale.isEmpty() ? null :
                sale.stream().map(SaleMapper::toDtoWithoutAssociations).toList();
    }
}
