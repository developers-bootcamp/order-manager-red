package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    @Autowired
    User getByAddressEmail(String email);

    @Autowired
    User getById(String id);

    @Autowired
    List<User> findByCompanyId_IdAndRoleId_Id(String companyId, String id);

}
