package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.User;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByFullName(String fullName);

    ResponseEntity findByRoleId_Name(String customer);

    boolean existsByAddress_Email(String email);

    Page<User> findAll(Pageable pageable);
    @Autowired
    User getByAddressEmail(String email);

    @Autowired
    List<User> getAllByCompanyId(String companyId);
}
