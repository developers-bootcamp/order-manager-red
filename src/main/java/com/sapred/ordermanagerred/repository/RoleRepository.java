package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.Role;
import com.sapred.ordermanagerred.model.RoleOptions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface RoleRepository extends MongoRepository<Role, String> {


     Role findOneById(String id);

    Role findFirstByName(RoleOptions name);
}
