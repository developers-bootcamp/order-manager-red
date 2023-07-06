package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.dto.productMapper;
import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.Company;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.repository.CompanyRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private JwtToken jwtToken;

    @SneakyThrows
    public Product addProduct(String token, Product product) {
        if (productRepository.existsByName(product.getName()) == true)
            throw new Exception();
        product.setAuditData(new AuditData(new Date(), null));
        Optional<Company> companyOptional = companyRepository.findById(jwtToken.getCompanyIdFromToken(token));
        Company company = companyOptional.orElseThrow(() -> new Exception("Company not found"));
        product.setCompanyId(company);
        return productRepository.insert(product);
    }

    @SneakyThrows
    public List<ProductNameDTO> getAllNamesProducts(String token, String prefix) {
        List<Product> products = productRepository.findByCompanyId_NameAndPrefix(token, prefix).stream().toList();
        if (products == null)
            throw new Exception();
        List<ProductNameDTO> productList = productMapper.INSTANCE.productToProductNameDto(products);
        return productList;
    }

    @SneakyThrows
    public List<ProductDTO> getAllProducts(String token) {
        String companyId = jwtToken.getCompanyIdFromToken(token);
        List<Product> products = productRepository.findAllByCompanyId(companyId).stream().toList();
        if (products == null)
            throw new Exception();
        //return HttpStatus.INTERNAL_SERVER_ERROR;
        List<ProductDTO> productDTOs = productMapper.INSTANCE.productToDto(products);
        return productDTOs;
    }

    @SneakyThrows
    public Product editProduct(Product product) {
        product.getAuditData().setUpdateDate(new Date());
        product = productRepository.save(product);
        return product;
    }

    public  HttpStatus deleteProduct(String id){
        if (productRepository.existsById(id))
            return HttpStatus.NOT_FOUND;
        productRepository.deleteById(id);
        return HttpStatus.OK;
    }
}
