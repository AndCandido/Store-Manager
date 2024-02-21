package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
<<<<<<< Updated upstream
=======
import io.github.AndCandido.storemanager.domain.dtos.requests.PaymentInstallment;
>>>>>>> Stashed changes
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
    public Installment getInstallmentById(UUID id) {
        return installmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada"));
    }

    @Override
    public Installment updateInstallment(UUID id, InstallmentRequestDto installmentRequestDto) {
        Installment installmentSaved = getInstallmentById(id);
        ApplicationUtil.copyNonNullProperties(installmentRequestDto, installmentSaved);
        return installmentRepository.save(installmentSaved);
    }

    @Override
    public Installment setPaymentInstallment(PaymentInstallment paymentInstallment, UUID id) {
        Installment installmentExisting = getInstallmentById(id);
        ApplicationUtil.copyNonNullProperties(paymentInstallment, installmentExisting);
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
