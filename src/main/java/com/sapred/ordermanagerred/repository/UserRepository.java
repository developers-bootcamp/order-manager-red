package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.User;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByAddress_Email(String email);

    User getByAddressEmail(String email);

    @Autowired
    @Query("{'companyId.id': ?0, 'roleId.id': ?1, 'fullName': { $regex: '^?2' }}}")
    List<User> findByCompanyIdAndRoleIdAndPrefix(String companyId, String roleId, String prefix);

}
