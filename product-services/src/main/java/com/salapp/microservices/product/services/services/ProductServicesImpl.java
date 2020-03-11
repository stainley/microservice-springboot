package com.salapp.microservices.product.services.services;

import com.salapp.microservices.api.core.product.Product;
import com.salapp.microservices.api.core.product.ProductService;
import com.salapp.microservices.util.core.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Stainley Lebron
 * @since 3/9/20.
 */
@RestController
public class ProductServicesImpl implements ProductService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServicesImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(String productId) {

        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }
}
