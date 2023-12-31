package com.sapred.ordermanagerred.repository;

import com.sapred.ordermanagerred.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByAddressEmail(String email);

    boolean existsByAddress_Email(String email);

    User getByAddressEmail(String email);

    @Query("{'companyId.id': ?0, 'roleId.id': ?1, 'fullName': { $regex: '^?2' }}")
    List<User> findByCompanyIdAndRoleIdAndPrefix(String companyId, String roleId, String prefix);
    Page<User> findByCompanyId(String companyId, Pageable pageable);

}
