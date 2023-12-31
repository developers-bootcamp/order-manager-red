package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    boolean existsByNameAndCompanyId(String name,String companyId);

    List<Product> findAllByCompanyId(String companyId);

    Product findOneByIdAndCompanyId(String id,String companyId);

    @Query("{'companyId.id': ?0, 'name': { $regex: '^?1' }}")
    List<Product> findByCompanyIdAndNameAndPrefix(String companyId, String prefix);

}
