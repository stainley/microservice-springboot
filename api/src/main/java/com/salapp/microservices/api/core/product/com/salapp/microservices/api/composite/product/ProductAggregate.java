package com.salapp.microservices.api.core.product.com.salapp.microservices.api.composite.product;

import java.util.List;

/**
 * @author Stainley Lebron
 * @since 3/9/20.
 */
public class ProductAggregate {
    private final int productId;
    private final String name;
    private final int weight;
    private final List<RecommendationSummary> recommendations;
    private final List<ReviewSummary> reviews;
    private final ServiceAddress serviceAddress;

    public ProductAggregate(int productId, String name, int weight, List<RecommendationSummary> recommendations, List<ReviewSummary> reviews, ServiceAddress serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.recommendations = recommendations;
        this.reviews = reviews;
        this.serviceAddress = serviceAddress;
    }
}
