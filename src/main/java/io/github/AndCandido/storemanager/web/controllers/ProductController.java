package io.github.AndCandido.storemanager.web.controllers;

import io.github.AndCandido.storemanager.domain.dtos.requests.ProductRequestDto;
import io.github.AndCandido.storemanager.domain.dtos.responses.ProductResponseDto;
import io.github.AndCandido.storemanager.domain.mappers.ProductMapper;
import io.github.AndCandido.storemanager.domain.models.Product;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> saveProduct(
        @RequestBody @Valid ProductRequestDto productRequestDto
    ) {
        Product productSaved = productService.saveProduct(productRequestDto);
        var productResponse = ProductMapper.toDto(productSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if(products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<ProductResponseDto>());
        }

        List<ProductResponseDto> productResDtos = products.stream()
                .map(ProductMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(productResDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        var productResponse = ProductMapper.toDto(product);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
        @RequestBody @Valid ProductRequestDto productRequestDto,
        @PathVariable Long id
    ) {
        Product product = productService.updateProduct(productRequestDto, id);
        var productResponse = ProductMapper.toDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
