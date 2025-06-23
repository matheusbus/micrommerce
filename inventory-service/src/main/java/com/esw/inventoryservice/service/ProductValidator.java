package com.esw.inventoryservice.service;

import com.esw.inventoryservice.exception.NotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductValidator {

    private final RestTemplate restTemplate;

    public ProductValidator(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public void validateProductExists(Long productId) {
        try {
            String url = "http://localhost:8081/api/products/" + productId;
            restTemplate.getForObject(url, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new NotFoundException(String.format("%s not found with %s = '%s'.",
                    "Product", "id", productId.toString()));
        }
    }
}
