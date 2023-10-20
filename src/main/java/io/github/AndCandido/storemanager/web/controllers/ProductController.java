package io.github.AndCandido.storemanager.web.controllers;


import io.github.AndCandido.storemanager.domain.dtos.ProductDto;
import io.github.AndCandido.storemanager.domain.models.ProductModel;
import io.github.AndCandido.storemanager.domain.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ResponseEntity<ProductModel> saveProduct(@RequestBody @Valid ProductDto productDto) {
        ProductModel productSaved = productService.saveProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productSaved);
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        List<ProductModel> products = productService.getAllProducts();

        HttpStatus httpStatus = products.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return ResponseEntity.status(httpStatus).body(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable Long id) {
        ProductModel product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> updateProduct(
            @RequestBody @Valid ProductDto productDto, @PathVariable Long id
    ) {
        ProductModel product = productService.updateProduct(productDto, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
