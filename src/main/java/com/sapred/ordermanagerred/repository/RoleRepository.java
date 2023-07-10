package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Role;
import com.sapred.ordermanagerred.model.RoleOptions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findFirstByName(RoleOptions name);
}
