package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Orders, String> {
    //should it be also filterd also by status????
    //findByCompanyIdAndOrderStatusId(String companyId,String orderStatusId,Pageable pageable);
    Page<Orders> findByCompanyId(String companyId,Pageable pageable);
    Page<Orders> findAll(Pageable pageable);
}
