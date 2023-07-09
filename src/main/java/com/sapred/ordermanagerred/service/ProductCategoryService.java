package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.Exception.AccessPermissionException;
import com.sapred.ordermanagerred.Exception.DataExistException;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private JwtToken jwtToken;

    public void createProductCategory(ProductCategory productCategory, String token) {
        if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)))
            throw new AccessPermissionException("You do not have permission to create new category");
        if (productCategoryRepository.existsByName(productCategory.getName()))
            throw new DataExistException("the name of the category already exist");
        productCategory.setAuditData(new AuditData(new Date(), new Date()));
        productCategoryRepository.save(productCategory);
    }
}
