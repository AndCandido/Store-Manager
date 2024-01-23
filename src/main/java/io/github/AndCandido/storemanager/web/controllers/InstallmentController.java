package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.InstallmentResponseDto;
import io.github.AndCandido.storemanager.domain.dtos.groups.ToPatchGroup;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
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
    public InstallmentResponseDto patchInstallment(
        @RequestBody @Validated(ToPatchGroup.class) InstallmentRequestDto installmentRequestDto,
        @PathVariable UUID id)
    {
        var installmentUpdated = installmentService.patchInstallment(installmentRequestDto, id);
        return InstallmentMapper.toDto(installmentUpdated);
    }
}
