package com.techpractice.product_service.persistance;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.testcontainers.shaded.org.yaml.snakeyaml.constructor.DuplicateKeyException;
import reactor.test.StepVerifier;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class PersistenceTests extends MongoDBTestBase {
    @Autowired
    private ProductRepository productRepository;

    private ProductEntity savedEntity;

    @BeforeEach
    public void dataSetUp(){
        productRepository.deleteAll();
        ProductEntity entity = new ProductEntity(1, "test name", 1);
        StepVerifier.create(productRepository.save(entity))
                .expectNextMatches(createdEntity ->{
                    savedEntity = createdEntity;
                    return areProductEqual(entity, savedEntity);
                }).verifyComplete();


    }

    @Test
    void create(){
        ProductEntity newEntity = new ProductEntity(2, "n", 2);
        StepVerifier.create(productRepository.save(newEntity))
                .expectNextMatches(createdEntity -> newEntity.getProductId() == createdEntity.getProductId())
                .verifyComplete();

        StepVerifier.create(productRepository.findByProductId(newEntity.getProductId()))
                .expectNextMatches(foundEntity -> areProductEqual(newEntity, foundEntity))
                .verifyComplete();
    }

    @Test
    void update(){
        savedEntity.setName("n2");
        StepVerifier.create(productRepository.save(savedEntity))
                .expectNextMatches(updatedEntity -> updatedEntity.getName().equals("n2"))
                .verifyComplete();
        StepVerifier.create(productRepository.findById(savedEntity.getId()))
                .expectNextMatches(foundEntity ->foundEntity.getVersion() ==1 && foundEntity.getName().equals("n2"))
                .verifyComplete();
    }


    @Test
    void delete(){
        StepVerifier.create(productRepository.delete(savedEntity)).verifyComplete();
        StepVerifier.create(productRepository.existsById(savedEntity.getId())).expectNext(false).verifyComplete();
    }

    @Test
    void getByProductId(){
        StepVerifier.create(productRepository.findByProductId(savedEntity.getProductId()))
                .expectNextMatches(foundEntity -> areProductEqual(savedEntity, foundEntity))
                .verifyComplete();
    }

    @Test
    void duplicateError(){
        ProductEntity entity = new ProductEntity(savedEntity.getProductId(), "test name", 1);
        StepVerifier.create(productRepository.save(entity)).expectError(DuplicateKeyException.class).verify();
    }

    @Test
    void optimisticLockError(){
        ProductEntity entity1 = productRepository.findById(savedEntity.getId()).block();
        ProductEntity entity2 = productRepository.findById(savedEntity.getId()).block();

        entity1.setName("n1");
        productRepository.save(entity1).block();
        //  Update the entity using the second entity object.
        // This should fail since the second entity now holds a old version number, i.e. a Optimistic Lock Error
        StepVerifier.create(productRepository.save(entity2)).expectError(OptimisticLockingFailureException.class).verify();
        // Get the updated entity from the database and verify its new sate
        StepVerifier.create(productRepository.findById(savedEntity.getId()))
                .expectNextMatches(foundEntity ->
                        foundEntity.getVersion() == 1
                                && foundEntity.getName().equals("n1"))
                .verifyComplete();
    }

    private boolean areProductEqual(ProductEntity expectedEntity, ProductEntity actualEntity) {
        return
                (expectedEntity.getId().equals(actualEntity.getId()))
                        && (expectedEntity.getVersion()==actualEntity.getVersion())
                && (expectedEntity.getProductId() == actualEntity.getProductId())
                && (expectedEntity.getName().equals(actualEntity.getName()))
                && (expectedEntity.getWeight()==actualEntity.getWeight());
    }


}
