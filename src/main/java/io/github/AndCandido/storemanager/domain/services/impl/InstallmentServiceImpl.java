package io.github.AndCandido.storemanager.domain.services.impl;

import io.github.AndCandido.storemanager.api.exceptions.IllegalClientActionException;
import io.github.AndCandido.storemanager.api.exceptions.ResourceNotFoundException;
import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.repositories.IInstallmentRepository;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InstallmentServiceImpl implements IInstallmentService {

    private IInstallmentRepository installmentRepository;

    public InstallmentServiceImpl(IInstallmentRepository installmentRepository) {
        this.installmentRepository = installmentRepository;
    }

    @Override
    public Installment saveInstallment(Installment installment) {
        if(installment.isPaid() && installment.getPaymentMethod() == null) {
            throw new IllegalClientActionException("Deve se informado a forma de pagamento quando a parcela é paga");
        }

        return installmentRepository.save(installment);
    }

    @Override
    public Installment findById(UUID id) {
        return installmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parcela não encontrada"));
    }

    @Override
    public List<Installment> saveInstallmentBySale(Sale sale, SaleDto saleDto) {
        var installments = new ArrayList<Installment>(saleDto.installments().size());

        for (InstallmentDto installmentDto : saleDto.installments()) {
            var installment = InstallmentMapper.toModel(installmentDto);

            installment.setCustomer(sale.getCustomer());
            installment.setSale(sale);

            installments.add(installment);
        }

        return installments;
    }



}
