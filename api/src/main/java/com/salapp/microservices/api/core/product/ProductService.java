package com.salapp.microservices.api.core.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Stainley Lebron
 * @since 3/9/20.
 */
public interface ProductService {

    @GetMapping(value = "/product/{productId}", produces = "application/json;charset=UTF-8")
    @ResponseBody
    Product getProduct(@PathVariable String productId);
}
