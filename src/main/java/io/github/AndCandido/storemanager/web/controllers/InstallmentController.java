package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.requests.InstallmentRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.requests.PaymentInstallment;
import io.github.AndCandido.storemanager.domain.dtos.responses.InstallmentResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.InstallmentMapper;
import io.github.AndCandido.storemanager.domain.models.Installment;
import io.github.AndCandido.storemanager.domain.services.IInstallmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/installments")
@RequiredArgsConstructor
public class InstallmentController {

    private final IInstallmentService installmentService;

    @PutMapping("/{id}")
    public ResponseEntity<InstallmentResponseDto> updatedInstallment(
        @RequestBody @Valid InstallmentRequestDto installmentRequestDto,
        @PathVariable UUID id
    ) {
        Installment installmentUpdated = installmentService.updateInstallment(id, installmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstallmentMapper.toDto(installmentUpdated));
    }

    @PatchMapping("/payment/{id}")
    public ResponseEntity<InstallmentResponseDto> patchInstallment(
        @RequestBody @Valid PaymentInstallment paymentInstallment,
        @PathVariable UUID id
    ) {
        var installmentUpdated = installmentService.setPaymentInstallment(paymentInstallment, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(InstallmentMapper.toDto(installmentUpdated));
    }
}
