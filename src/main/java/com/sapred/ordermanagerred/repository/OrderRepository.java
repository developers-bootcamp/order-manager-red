package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.time.Month;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

//    List<Order> findByAuditDataCreateDateMonth(Month currentMonth);
List<Order> findByAuditDataCreateDate(LocalDate date);


    Page<Order> findByCompanyId_IdAndOrderStatusIdAndEmployee(String companyId, String orderStatusId, String employee, Pageable pageable);
}
