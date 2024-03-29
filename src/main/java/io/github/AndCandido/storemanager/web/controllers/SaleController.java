package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.requests.SaleRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.SaleResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.SaleMapper;
import io.github.AndCandido.storemanager.domain.models.Sale;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final ISaleService saleService;

    @PostMapping
    public ResponseEntity<SaleResponseDto> saveSale(
        @RequestBody @Valid SaleRequestDto saleRequestDto
    ) {
        Sale sale = saleService.saveSale(saleRequestDto);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResDto);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();

        if(sales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<SaleResponseDto>());
        }

        List<SaleResponseDto> saleResDtos = sales.stream()
                .map(SaleMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(saleResDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable UUID id) {
        Sale sale = saleService.getSaleById(id);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.ok(saleResDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleResponseDto> updateSale(
            @RequestBody @Valid SaleRequestDto saleRequestDto,
            @PathVariable UUID id
    ) {
        Sale sale = saleService.updateSale(saleRequestDto, id);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable UUID id) {
        saleService.deleteSale(id);
    }
}
