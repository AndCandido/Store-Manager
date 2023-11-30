package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/installments")
public class InstallmentController {

    private IInstallmentService installmentService;

    public InstallmentController(IInstallmentService installmentService) {
        this.installmentService = installmentService;
    }

    @PatchMapping("{id}")
    public InstallmentDto patchInstallment(@RequestBody InstallmentDto installmentDto, @PathVariable UUID id) {
        var existingInstallment = installmentService.findById(id);

        ApplicationUtil.copyNonNullProperties(installmentDto, existingInstallment);

        var installmentUpdated = installmentService.saveInstallment(existingInstallment);

        return InstallmentMapper.toDto(installmentUpdated);
    }

}
