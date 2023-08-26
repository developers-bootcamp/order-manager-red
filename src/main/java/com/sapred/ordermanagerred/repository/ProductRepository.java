package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByName(String name);

    List<Product> findAllByCompanyId(String companyId);

    Product findOneByIdAndCompanyId(String id,String companyId);

    @Query("{'name': { $regex: '^?1' }}")
    List<Product> findByCompanyIdAndNameAndPrefix(String companyId, String prefix);

}
