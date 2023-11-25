package io.github.AndCandido.storemanager.web.controllers;


import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.mappers.ProductMapper;
import io.github.AndCandido.storemanager.domain.models.Product;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@RequestBody @Valid ProductDto productDto) {
        Product productSaved = productService.saveProduct(productDto);
        var productResponse = ProductMapper.toDto(productSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        if(products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<ProductDto>());
        }

        List<ProductDto> productResDtos = products.stream()
                .map(ProductMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(productResDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        var productResponse = ProductMapper.toDto(product);
        return ResponseEntity.ok(productResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @RequestBody @Valid ProductDto productDto, @PathVariable Long id
    ) {
        Product product = productService.updateProduct(productDto, id);
        var productResponse = ProductMapper.toDto(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
