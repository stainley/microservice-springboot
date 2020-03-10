package com.salapp.microservices.api.core.product.com.salapp.microservices.api.composite.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Stainley Lebron
 * @since 3/9/20.
 */

public interface ProductCompositeService {
    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    ProductAggregate getProduct(@PathVariable int productId);
}
