package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);

}
