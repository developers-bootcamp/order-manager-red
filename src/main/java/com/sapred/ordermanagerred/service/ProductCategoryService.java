package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.mapper.ProductCategoryMapper;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.ProductCategoryRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class ProductCategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    public void createProductCategory(ProductCategory productCategory, String token) {
        log.info("Creating new product category");

        String companyId = jwtToken.getCompanyIdFromToken(token);
        if (productCategoryRepository.existsByNameAndCompanyId_id(productCategory.getName(), companyId)) {
            log.error("Data exist: The name of the category already exists");
            throw new DataExistException("The name of the category already exists");
        }

        productCategory.setAuditData(AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
        productCategory.setCompanyId(companyRepository.findFirstById(companyId));
        productCategoryRepository.save(productCategory);

        log.info("Product category created successfully");
    }

    public void deleteProductCategory(String token, String id) {
        log.info("Deleting product category");

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        ProductCategory productCategory = productCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product category not found"));

        if (role == RoleOptions.CUSTOMER || !productCategory.getCompanyId().getId().equals(companyIdFromToken)) {
            log.error("Permission denied: You do not have the appropriate permission to delete product category");
            throw new NoPermissionException("You do not have the appropriate permission to delete product category");
        }

        productCategoryRepository.deleteById(id);

        log.info("Product category deleted successfully");
    }

    public List<ProductCategoryDto> getAllCategory(String token) {
        log.info("Retrieving all product categories");

        RoleOptions role = jwtToken.getRoleIdFromToken(token);
        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);

        if (role == RoleOptions.CUSTOMER) {
            log.error("Permission denied: You do not have the appropriate permission to get all product categories");
            throw new NoPermissionException("You do not have the appropriate permission to get all product categories");
        }

        List<ProductCategory> productCategories = productCategoryRepository.getAllByCompanyId(companyIdFromToken);
        List<ProductCategoryDto> productCategoryDtos = productCategoryMapper.productCategoryDtoToProductCategory(productCategories);

        log.info("Retrieved {} product categories", productCategoryDtos.size());

        return productCategoryDtos;
    }

    public void editProductCategory(String token, ProductCategoryDto productCategoryDto) {
        log.info("Editing product category");

        ProductCategory productCategory = productCategoryRepository.findById(productCategoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Product category not found"));

        if (!productCategory.getCompanyId().getId().equals(jwtToken.getCompanyIdFromToken(token)) || jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER) {
            log.error("Permission denied: You do not have permission to edit the product category");
            throw new NoPermissionException("You do not have permission to edit the product category");
        }

        productCategory.setName(productCategoryDto.getName());
        productCategory.setDesc(productCategoryDto.getDesc());
        productCategory.getAuditData().setUpdateDate((LocalDate.now()));
        productCategoryRepository.save(productCategory);

        log.info("Product category edited successfully");
    }
}
