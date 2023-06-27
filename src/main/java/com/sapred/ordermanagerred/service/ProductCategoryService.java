package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
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

    //יצירת מופע של productCategory
    public ResponseEntity<String> createProductCategory(ProductCategory productCategory){
        try {
            if(productCategoryRepository.existsByName(productCategory.getName()))
                throw new ResponseStatusException(HttpStatus.CONFLICT,"the name of the category already exists");
            AuditData auditData=new AuditData();
            auditData.setCreateDate(new Date());
//            auditData.setUpdateData(new Date());
            productCategory.setAuditData(auditData);
            productCategoryRepository.save(productCategory);
            return ResponseEntity.ok("success: true");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error create productCategory"+ e.getMessage());
        }
    }
}
