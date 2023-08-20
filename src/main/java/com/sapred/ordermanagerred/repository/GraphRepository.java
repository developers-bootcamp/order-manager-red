package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GraphRepository extends MongoRepository<Order,String> {

}
