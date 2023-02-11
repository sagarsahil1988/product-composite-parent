package com.techpractice.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCompositeService {

    @GetMapping(value = "/product-composite/{productId}",
    produces = "application/json")
    ProductAggregate getProductAggregate(@PathVariable int productId);

    @GetMapping(value = "/test",
    produces = "application/json")
    String testMethod();

}
