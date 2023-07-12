package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.dto.productMapper;
import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.exception.ObjectAlreadyExists;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private JwtToken jwtToken;

    @Autowired
    CompanyService companyService;

    @SneakyThrows
    public Product addProduct(String token, Product product) {
        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN)
            throw new NoPermissionException("You can't add product");
        if (productRepository.existsByName(product.getName()))
            throw new ObjectAlreadyExists("The product already exists");
        product.setAuditData(new AuditData(new Date(), new Date()));
        Company company = companyService.getCompany(jwtToken.getCompanyIdFromToken(token));
        product.setCompanyId(company);
        return productRepository.insert(product);
    }

    @SneakyThrows
    public List<ProductNameDTO> getAllNamesProducts(String token, String prefix) {
        if (jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER)
            throw new NoPermissionException("You can't get name's products");
        List<Product> products = productRepository.findByCompanyIdAndNameAndPrefix(token, prefix).stream().toList();
        List<ProductNameDTO> productList = productMapper.INSTANCE.productToProductNameDto(products);
        return productList;
    }

    @SneakyThrows
    public List<ProductDTO> getAllProducts(String token) {
        if (jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER)
            throw new NoPermissionException("You can't get products");
        String companyId = jwtToken.getCompanyIdFromToken(token);
        List<Product> products = productRepository.findAllByCompanyId(companyId).stream().toList();
        List<ProductDTO> productDTOs = productMapper.INSTANCE.productToDto(products);
        return productDTOs;
    }

    @SneakyThrows
    public Product editProduct(String token, Product product) {
        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN)
            throw new NoPermissionException("You can't edit product");
        Optional<Product> productOptional = productRepository.findById(product.getId());
        Product productToUpdate = productOptional.orElseThrow(() -> new Exception("Company not found"));
        if (!productToUpdate.getName().equals(product.getName()) &&productRepository.existsByName(product.getName()) )
            throw  new ObjectAlreadyExists("You need unique name for product");
        productToUpdate.getAuditData().setUpdateDate(new Date());
        productToUpdate = productRepository.save(product);
        return productToUpdate;
    }

    @SneakyThrows
    public void deleteProduct(String token, String id) {
        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN)
            throw new NoPermissionException("You can't delete products");
        if (productRepository.existsById(id) == false) throw new Exception("Not found");
        productRepository.deleteById(id);
    }
}