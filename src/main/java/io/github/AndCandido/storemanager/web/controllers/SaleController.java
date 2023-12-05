package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
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
    public ResponseEntity<SaleDto> saveSale(@RequestBody @Valid SaleDto saleDto) {
        Sale sale = saleService.saveSale(saleDto);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResDto);
    }

    @GetMapping
    public ResponseEntity<List<SaleDto>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();

        if(sales.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<SaleDto>());
        }

        List<SaleDto> saleResDtos = sales.stream()
                .map(SaleMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(saleResDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> getSaleById(@PathVariable UUID id) {
        Sale sale = saleService.getSaleById(id);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.ok(saleResDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleDto> updateSale(
            @RequestBody @Valid SaleDto saleDto,
            @PathVariable UUID id
    ) {
        Sale sale = saleService.updateSale(saleDto, id);
        var saleResDto = SaleMapper.toDto(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(saleResDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable UUID id) {
        saleService.deleteSale(id);
    }
}
