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

    public static InstallmentDto toDtoWithoutAssociations(Installment installment) {
        if(installment == null) return null;

        return InstallmentDto.builder()
                .id(installment.getId())
                .dueDate(installment.getDueDate())
                .price(installment.getPrice())
                .paymentMethod(installment.getPaymentMethod())
                .isPaid(installment.isPaid())
                .createdAt(installment.getCreatedAt())
                .build();
    }

    public static List<InstallmentDto> toDtoListWithoutAssociations(List<Installment> installments) {
        if(installments == null || installments.isEmpty()) return null;

        return installments.stream().map(InstallmentMapper::toDtoWithoutAssociations).toList();
    }
}
