package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.exception.NoPremissionException;
import com.sapred.ordermanagerred.exception.ObjectDoesNotExistException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;

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

import java.io.ObjectStreamException;
import java.util.Date;
import java.util.List;

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
    @Autowired
    private JwtToken jwtToken;
    private ProductCategoryMapper productCategoryMapper;


    //יצירת מופע של productCategory
    public ResponseEntity<String> createProductCategory(ProductCategory productCategory) {
        try {
            if (productCategoryRepository.existsByName(productCategory.getName()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "the name of the category already exists");
            AuditData auditData = new AuditData();
            auditData.setCreateDate(new Date());
            auditData.setUpdateDate(new Date());
            productCategory.setAuditData(auditData);
            productCategoryRepository.save(productCategory);
            return ResponseEntity.ok("success: true");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error create productCategory" + e.getMessage());
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

    public void editProductCategory(String token, ProductCategory productCategory) {
       if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)) || jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER)
            throw new NoPremissionException("You dont have permission to delete the product");
        if (productCategoryRepository.existsById(productCategory.getId()) == true)
            throw new ObjectDoesNotExistException("This object does not exist");
        productCategory.getAuditData().setUpdateDate(new Date());
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
