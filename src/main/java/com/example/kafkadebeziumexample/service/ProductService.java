package com.example.kafkadebeziumexample.service;

import com.example.kafkadebeziumexample.dto.ProductSaveRequest;
import com.example.kafkadebeziumexample.dto.ProductUpdateRequest;
import com.example.kafkadebeziumexample.model.Product;
import com.example.kafkadebeziumexample.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Long save(ProductSaveRequest productSaveRequest) {
        Product product = new Product(productSaveRequest);
        productRepository.save(product);
        return product.getId();
    }

    public String update(ProductUpdateRequest productUpdateRequest) {
        Optional<Product> productOptional = productRepository.findById(productUpdateRequest.getId());
        if(productOptional.isPresent())
        {
            Product product = productOptional.get();
            product.setName(productUpdateRequest.getName());
            product.setPrice(productUpdateRequest.getPrice());
            productRepository.save(product);
        }
        return productUpdateRequest.getName();
    }

    public Long delete(Long id) {
        Product product = productRepository.findById(id).get();
        productRepository.delete(product);
        return product.getId();
    }
}
