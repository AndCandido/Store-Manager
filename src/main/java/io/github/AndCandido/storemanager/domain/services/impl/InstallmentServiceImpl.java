package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.IInstallmentRepository;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstallmentServiceImpl implements IInstallmentService {

    private final IInstallmentRepository installmentRepository;

    @Override
    public Installment saveInstallment(Installment installment) {
        return installmentRepository.save(installment);
    }

    @Override
    public Installment findById(UUID id) {
        return installmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada"));
    }

    @Override
    public Installment patchInstallment(InstallmentDto installmentDto, UUID id) {
        var installmentExisting = findById(id);

        ApplicationUtil.copyNonNullProperties(installmentDto, installmentExisting);

        return saveInstallment(installmentExisting);
    }

    @Override
    public Installment createInstallment(InstallmentDto installmentDto) {
        return InstallmentMapper.toModel(installmentDto);
    }
}
