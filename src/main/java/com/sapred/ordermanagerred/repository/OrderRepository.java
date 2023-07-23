package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Page<Order> findByCompanyId_IdAndOrderStatusAndEmployeeId(String companyId, String orderStatusId, String employee, Pageable pageable);

}
