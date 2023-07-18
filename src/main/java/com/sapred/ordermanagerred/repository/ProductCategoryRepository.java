package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.ProductCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

    boolean existsByNameAndCompanyId_id(String name, String companyId);

    List<ProductCategory> getAllByCompanyId(String id);

}
