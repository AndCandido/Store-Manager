package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;

import java.util.List;

public class SaleMapper {

    public static SaleDto toDto(Sale sale) {
        if(sale == null) return null;

        var customerResponse = CustomerMapper.toSaleResponseDto(sale.getCustomer());
        var productsSoldDto = ProductSoldMapper.toDtoList(sale.getProductsSold());
        var installmentsDto = InstallmentMapper.toResponseDtoList(sale.getInstallments());

        return new SaleDto(
                sale.getId(),
                customerResponse,
                productsSoldDto,
                installmentsDto,
                sale.getPrice(),
                sale.getCreatedAt()
        );
    }


    public static SaleDto toCustomerResponseDto(Sale sale) {
        if(sale == null) return null;

        return new SaleDto(
                sale.getId(),
                null,
                null,
                null,
                sale.getPrice(),
                sale.getCreatedAt()
        );
    }

    public static Sale toModel(SaleDto saleDto) {
        var saleModel = new Sale();
        ApplicationUtil.copyNonNullProperties(saleDto, saleModel);
        return saleModel;
    }

    public static List<SaleDto> toCustomerResponseDtoList(List<Sale> sale) {
        return sale == null || sale.isEmpty() ? null :
                sale.stream().map(SaleMapper::toCustomerResponseDto).toList();
    }
}
