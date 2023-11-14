package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;

import java.util.List;

public class SaleMapper {
    public static SaleDto toDto(SaleModel saleModel) {
        var productSoldModels = saleModel.getProductsSold();
        var productSoldDtos = productSoldModels == null || productSoldModels.isEmpty() ? null
                : ProductSoldMapper.toDtoList(saleModel.getProductsSold());

        var customerResponse = saleModel.getCustomer() == null ? null :
                CustomerMapper.toResponseDto(saleModel.getCustomer());

        return new SaleDto(
                saleModel.getId(),
                saleModel.getDuplication(),
                saleModel.getPaymentMethod(),
                customerResponse,
                productSoldDtos,
                saleModel.getPrice(),
                saleModel.getCreatedAt()
        );
    }

    public static SaleDto toResponseDto(SaleModel saleModel) {
        return new SaleDto(
                saleModel.getId(),
                saleModel.getDuplication(),
                saleModel.getPaymentMethod(),
                null,
                null,
                saleModel.getPrice(),
                saleModel.getCreatedAt()
        );
    }


    public static SaleModel toModel(SaleDto saleDto) {
        var saleModel = new SaleModel();
        ApplicationUtil.copyNonNullProperties(saleDto, saleModel);
        return saleModel;
    }

    public static List<SaleDto> toResponseDtoList(List<SaleModel> saleModel) {
        return saleModel == null ? null :
                saleModel.stream().map(SaleMapper::toResponseDto).toList();
    }

    public static List<SaleModel> toModelList(List<SaleDto> saleDtos) {
        return saleDtos == null ? null :
                saleDtos.stream().map(SaleMapper::toModel).toList();
    }
}
