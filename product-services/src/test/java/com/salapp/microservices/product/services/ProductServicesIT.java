package com.salapp.microservices.product.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Stainley Lebron
 * @since 3/10/20.
 */

public class ProductServicesIT {

    @Test
    public void sayHello(){
        Assertions.assertThat("Hello").isEqualTo("Hello");
    }
}
