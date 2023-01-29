package com.techpractice.product_service.services;

import com.techpractice.api.core.product.Product;
import com.techpractice.api.core.product.ProductService;
import com.techpractice.utils.exceptions.InvalidInputException;
import com.techpractice.utils.exceptions.NotFoundException;
import com.techpractice.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

//    To use ServiceUtil class we have to inject into constructor.
    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        LOG.debug("Product Returned for productId = {}",productId);
        if(productId <  1 ) throw new InvalidInputException("Invalid productId: "+productId);
        if(productId == 13) throw new NotFoundException("No product found for productId: "+ productId);

        return new Product(1, "Test Product" + productId, 100, serviceUtil.getServiceAddress());

    }
}
