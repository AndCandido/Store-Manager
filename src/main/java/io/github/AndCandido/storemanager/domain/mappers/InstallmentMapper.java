package io.github.AndCandido.storemanager.domain.mappers;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.models.Installment;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class InstallmentMapper {
    public static Installment toModel(InstallmentDto installmentDto) {
        var installment = new Installment();
        BeanUtils.copyProperties(installmentDto, installment);
        return installment;
    }

    public static InstallmentDto toResponseDto(Installment installment) {
        if(installment == null) return null;

        return new InstallmentDto(
                installment.getId(),
                installment.getDueDate(),
                installment.getPrice(),
                installment.getPaymentMethod(),
                installment.isPaid(),
                null,
                null,
                installment.getCreatedAt()
        );
    }

    public static List<InstallmentDto> toResponseDtoList(List<Installment> installments) {
        if(installments == null || installments.isEmpty()) return null;

        return installments.stream().map(InstallmentMapper::toResponseDto).toList();
    }
}
