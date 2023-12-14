package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.InstallmentDto;
import io.github.AndCandido.storemanager.domain.dtos.groups.ToPatch;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import io.github.AndCandido.storemanager.utils.ApplicationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/installments")
@RequiredArgsConstructor
public class InstallmentController {

    private final IInstallmentService installmentService;

    @PatchMapping("{id}")
    public InstallmentDto patchInstallment(@RequestBody @Validated(ToPatch.class) InstallmentDto installmentDto, @PathVariable UUID id) {
        var installmentUpdated = installmentService.patchInstallment(installmentDto, id);
        return InstallmentMapper.toDto(installmentUpdated);
    }

}
