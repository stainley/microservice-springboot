package com.salapp.microservices.product.composite.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.salapp.microservices")
public class ProductCompositeServicesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCompositeServicesApplication.class, args);
    }

}
