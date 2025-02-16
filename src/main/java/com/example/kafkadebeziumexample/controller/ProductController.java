package com.example.kafkadebeziumexample.controller;

import com.example.kafkadebeziumexample.dto.ProductSaveRequest;
import com.example.kafkadebeziumexample.dto.ProductUpdateRequest;
import com.example.kafkadebeziumexample.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody ProductSaveRequest productSaveRequest) {
        return ResponseEntity.ok(productService.save(productSaveRequest));
    }

    @PutMapping
    public ResponseEntity<String> update(@RequestBody ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(productService.update(productUpdateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
