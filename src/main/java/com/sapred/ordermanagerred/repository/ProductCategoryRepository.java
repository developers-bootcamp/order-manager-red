package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Order;
import com.sapred.ordermanagerred.model.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

    boolean existsByNameAndCompanyId_id(String name, String companyId);
    List<ProductCategory> getAllByCompanyId(String id);
    ProductCategory findByIdAndCompanyId_Id(String id, String companyId);


}
