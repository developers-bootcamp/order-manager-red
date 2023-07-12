package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.Mapper.ProductCategoryMapper;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public void deleteProductCategory(String token, String id) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        ProductCategory productCategory = productCategoryRepository.findById(id).get();

        if (role == RoleOptions.CUSTOMER || !productCategory.getCompanyId().getId().equals(companyIdFromToken))
            throw new NoPermissionException("You do not have the appropriate permission to delete product category");

        productCategoryRepository.deleteById(id);

    }
    public List<ProductCategoryDto> getAllCategory(String token) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        if (role == RoleOptions.CUSTOMER)
            throw new NoPermissionException("You do not have the appropriate permission to get all product category");
        List<ProductCategory> productCategories = productCategoryRepository.getAllByCompanyId(companyIdFromToken);
        List<ProductCategoryDto> productCategoryDtos = productCategoryMapper.productCategoryDtoToProductCategory(productCategories);
        return productCategoryDtos;
    }
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
}