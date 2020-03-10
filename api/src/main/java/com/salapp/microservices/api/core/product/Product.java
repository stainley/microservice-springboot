package com.salapp.microservices.api.core.product;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.boot.jackson.JsonComponent;

/**
 * @author Stainley Lebron
 * @since 3/9/20.
 */
@JsonPOJOBuilder
public class Product {
    private final String productId;
    private final String name;
    private final int weight;
    private final String serviceAddress;

    public Product(String productId, String name, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }
}
