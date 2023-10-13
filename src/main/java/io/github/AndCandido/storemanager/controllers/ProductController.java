package io.github.AndCandido.storemanager.controllers;


import io.github.AndCandido.storemanager.dtos.ProductDto;
import io.github.AndCandido.storemanager.models.Product;
import io.github.AndCandido.storemanager.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestBody @Valid ProductDto productDto) {
        Product productSaved = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.CREATED).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

}
