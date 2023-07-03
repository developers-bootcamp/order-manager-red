package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {;
    boolean existsByAddressEmail(String email);
}
