package com.techpractice.product_service;


import com.techpractice.product_service.persistance.ProductEntity;
import com.techpractice.product_service.persistance.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class PersistenceTests extends MongoDBTestBase{
    @Autowired
    private ProductRepository productRepository;

    private ProductEntity savedEntity;

    @BeforeEach
    public void dataSetUp(){
        productRepository.deleteAll();
        ProductEntity entity = new ProductEntity(1, "test name", 1);
        savedEntity = productRepository.save(entity);
        assertEqualProduct(entity, savedEntity);
    }

    @Test
    public void getByProductId(){
        Optional<ProductEntity> entity = productRepository.findByProductId(savedEntity.getProductId());
        Assertions.assertTrue(entity.isPresent());
    }

    private void assertEqualProduct(ProductEntity expectedEntity, ProductEntity actualEntity){
        Assertions.assertEquals(expectedEntity.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
        Assertions.assertEquals(expectedEntity.getProductId(), actualEntity.getProductId());
        Assertions.assertEquals(expectedEntity.getName(), actualEntity.getName());
        Assertions.assertEquals(expectedEntity.getWeight(), actualEntity.getWeight());
    }

}
