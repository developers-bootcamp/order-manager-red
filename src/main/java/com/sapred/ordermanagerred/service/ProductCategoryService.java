package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.Mapper.ProductCategoryMapper;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.DataExistException;
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

import java.time.LocalDate;
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

    public void createProductCategory(ProductCategory productCategory, String token) {
        if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)))
            throw new NoPermissionException("You do not have permission to create new category");
        if (productCategoryRepository.existsByNameAndCompanyId_id(productCategory.getName(),productCategory.getCompanyId().getId()))
            throw new DataExistException("the name of the category already exist");
        productCategory.setAuditData(AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
        productCategoryRepository.save(productCategory);
    }

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
}

