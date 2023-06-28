package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;


    //יצירת מופע של productCategory
    public ResponseEntity<String> createProductCategory(ProductCategory productCategory){
        try {
            if(productCategoryRepository.existsByName(productCategory.getName()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"the name of the category already exists");
            AuditData auditData=new AuditData();
            auditData.setCreateDate(new Date());
            auditData.setUpdateDate(new Date());
            productCategory.setAuditData(auditData);
            productCategoryRepository.save(productCategory);
            return ResponseEntity.ok("success: true");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error create productCategory"+ e.getMessage());
        }

    }
    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        Company c = new Company("5", "aaaaa", 55, d);
        companyRepository.save(c);
        Role roles = new Role("2", RoleOptions.ADMIN, "cust", d);
        roleRepository.save(roles);
        Address a = new Address("0580000000", "mezada 7", "aaa@aaa.aaa");
        User user = new User("7", "kkkk", "aa", a, roles, c, d);
        userRepository.save((user));
        ProductCategory productCategory = new ProductCategory("5", "aaa", "aaa", c, d);
        productCategoryRepository.save(productCategory);
    }
    public  HttpStatus editProductCategory( String token,ProductCategory productCategory){
//        if(!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)))
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
        productCategoryRepository.save(productCategory);
        return HttpStatus.OK;
    }
}
