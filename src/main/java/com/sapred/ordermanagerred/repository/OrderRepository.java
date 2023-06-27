package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Orders, String> {
    //also find by user id- employee field in the orders??????????
    Page<Orders> findByCompanyId_IdAndOrderStatusIdAndEmployee(String companyId,String  orderStatusId,String employee, Pageable pageable);

//    Page<Orders> findByCompanyId_IdAndOrderStatusId(String companyId,String  orderStatusId, Pageable pageable);
}
