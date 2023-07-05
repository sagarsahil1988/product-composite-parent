package com.techpractice.product_service.persistance;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@EnableMongoRepositories
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String> {
    Optional<ProductEntity> findByProductId(int productId);

}
