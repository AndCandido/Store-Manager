package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.models.Customer;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.repositories.IInstallmentRepository;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Installment patchInstallment(InstallmentRequestDto installmentRequestDto, UUID id) {
        var installmentExisting = findById(id);

        ApplicationUtil.copyNonNullProperties(installmentRequestDto, installmentExisting);

        return saveInstallment(installmentExisting);
    }

    @Override
    public List<Installment> getInstallmentsByCustomerNonPaid(Customer customer) {
        return installmentRepository.findByCustomerAndIsPaidFalse(customer);
    }


    @Override
    public List<Installment> getInstallmentsByCustomer(Customer customer) {
        return installmentRepository.findByCustomer(customer);
    }

    @Override
    public Installment createInstallment(InstallmentRequestDto installmentRequestDto) {
        return InstallmentMapper.toModel(installmentRequestDto);
    }
}
