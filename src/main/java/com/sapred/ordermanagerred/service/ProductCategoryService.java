package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.AAAMapper;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;

    public void fill() {
        AuditData d = new AuditData(new Date(), new Date());
        Company c = new Company("7", "AAAAAAAAA", 55, d);
        companyRepository.save(c);
        Role roles = new Role("3", RoleOptions.ADMIN, "cust", d);
        roleRepository.save(roles);
        Address a = new Address("0580000000", "mezada 7", "aaa");
        User user = new User("8", "A", "a", a, roles, c, d);
        userRepository.save((user));
        ProductCategory productCategory = new ProductCategory("6", "bc", "yu", c, d);
        productCategoryRepository.save(productCategory);
        ProductCategory productCategory1 = new ProductCategory("7", "as", "vg", c, d);
        productCategoryRepository.save(productCategory1);
    }

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

    public ResponseEntity<Boolean> deleteProductCategory(String token, String id) {
        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        ProductCategory productCategory = productCategoryRepository.findById(id).get();

        if (role == RoleOptions.CUSTOMER || !productCategory.getCompanyId().getId().equals(companyIdFromToken))
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        try {
            productCategoryRepository.deleteById(id);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    public List<ProductCategoryDto> getAllCategory(String token) {
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<ProductCategory> productCategories = productCategoryRepository.getAllByCompanyId(companyIdFromToken);
        List<ProductCategoryDto> productCategoryDtos =
                 productCategories.stream()
                        .map(productCategory -> AAAMapper.INSTANCE.productCategoryToProductCategoryDto(productCategory))
                        .collect(Collectors.toList());
        return productCategoryDtos;
    }

}
