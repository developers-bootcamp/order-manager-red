package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.AuditData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditDataRepository extends MongoRepository<AuditData,String> {
}
