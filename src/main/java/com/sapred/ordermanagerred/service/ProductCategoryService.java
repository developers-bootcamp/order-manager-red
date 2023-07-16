package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.exception.ObjectDoesNotExistException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.RoleRepository;
import com.sapred.ordermanagerred.repository.UserRepository;

import com.sapred.ordermanagerred.Mapper.ProductCategoryMapper;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.security.JwtToken;
import org.bson.codecs.jsr310.LocalDateCodec;
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
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtToken jwtToken;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    //יצירת מופע של productCategory
    public void createProductCategory(ProductCategory productCategory, String token) {
        if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)))
            throw new NoPermissionException("You do not have permission to create new category");
        if (productCategoryRepository.existsByNameAndCompanyId_id(productCategory.getName(), productCategory.getCompanyId().getId()))
            throw new DataExistException("the name of the category already exist");
        productCategory.setAuditData(AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
        productCategoryRepository.save(productCategory);
    }

    public void fill() {
        AuditData d = new AuditData(LocalDate.now(),LocalDate.now());
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

    public void editProductCategory(String token, ProductCategoryDto productCategoryDto) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryDto.getId())
                .orElseThrow(() -> new ObjectDoesNotExistException("This object does not exist"));
        if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)) || jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER) {
            throw new NoPermissionException("You dont have permission to edit the productCategory");
        }
        productCategory.setName(productCategoryDto.getName());
        productCategory.setDesc(productCategoryDto.getDesc());
        productCategory.getAuditData().setUpdateDate((LocalDate.now()));
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

