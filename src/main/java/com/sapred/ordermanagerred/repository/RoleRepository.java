package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,String> {
}
