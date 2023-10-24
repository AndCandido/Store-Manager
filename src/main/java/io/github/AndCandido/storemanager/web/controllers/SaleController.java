package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.SaleDto;
import io.github.AndCandido.storemanager.domain.models.SaleModel;
import io.github.AndCandido.storemanager.domain.services.ISaleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private ISaleService saleService;

    public SaleController(ISaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<SaleModel> saveSale(@RequestBody @Valid SaleDto saleDto) {
        SaleModel sale = saleService.saveSale(saleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    @GetMapping
    public ResponseEntity<List<SaleModel>> getAllSales() {
        List<SaleModel> sales = saleService.getAllSales();

        return ResponseEntity.status(HttpStatus.OK).body(sales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleModel> getSaleById(@PathVariable UUID id) {
        SaleModel sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SaleModel> updateSale(
            @RequestBody @Valid SaleDto saleDto,
            @PathVariable UUID id
    ) {
        SaleModel sale = saleService.updateSale(saleDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(sale);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSale(@PathVariable UUID id) {
        saleService.deleteSale(id);
    }
}
