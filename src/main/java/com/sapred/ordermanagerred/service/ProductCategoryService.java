package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.security.JwtToken;
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
    private JwtToken jwtToken;

    public ResponseEntity<String> createProductCategory(ProductCategory productCategory, String token){
        try {
            if(!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this resource.");
            if(productCategoryRepository.existsByName(productCategory.getName()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"the name of the category already exists");
            productCategory.setAuditData(new AuditData(new Date(),new Date()));
            productCategoryRepository.save(productCategory);
            return ResponseEntity.ok("success: true");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error create productCategory"+ e.getMessage());
        }
    }
}
