package com.esw.productservice.factory;

import com.esw.productservice.dto.product.ProductCreateRequest;
import com.esw.productservice.model.Product;

public interface ProductFactory {
    Product createProduct(ProductCreateRequest request);
}
