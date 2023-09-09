package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.mapper.productMapper;
import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.*;
import com.sapred.ordermanagerred.repository.ProductRepository;
import com.sapred.ordermanagerred.security.JwtToken;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @SneakyThrows
    public Product addProduct(String token, Product product) {
        log.info("Adding new product");

        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN) {
            log.error("Permission denied: You can't add product");
            throw new NoPermissionException("You can't add product");
        }

        if (productRepository.existsByNameAndCompanyId(product.getName(),jwtToken.getCompanyIdFromToken(token))) {
            log.error("Data exist: The product already exists");
            throw new DataExistException("The product already exists");
        }

        product.setAuditData(AuditData.builder().updateDate(LocalDate.now()).createDate(LocalDate.now()).build());
        Company company = companyService.getCompany(jwtToken.getCompanyIdFromToken(token));
        product.setCompanyId(company);
        return productRepository.insert(product);
    }

    @SneakyThrows
    public List<ProductNameDTO> getAllNamesProducts(String token, String prefix) {
        log.info("Getting names of products");

        if (jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER) {
            log.error("Permission denied: You can't get name's products");
            throw new NoPermissionException("You can't get name's products");
        }

        String companyIdFromToken = jwtToken.getCompanyIdFromToken(token);
        List<Product> products = productRepository.findByCompanyIdAndNameAndPrefix(companyIdFromToken, prefix);
        List<ProductNameDTO> productList = productMapper.INSTANCE.productToProductNameDto(products);

        log.info("Retrieved {} product names", productList.size());

        return productList;
    }

    @SneakyThrows
    public List<ProductDTO> getAllProducts(String token) {
        log.info("Getting all products");

        if (jwtToken.getRoleIdFromToken(token) == RoleOptions.CUSTOMER) {
            log.error("Permission denied: You can't get products");
            throw new NoPermissionException("You can't get products");
        }

        String companyId = jwtToken.getCompanyIdFromToken(token);
        List<Product> products = productRepository.findAllByCompanyId(companyId);
        List<ProductDTO> productDTOs = productMapper.INSTANCE.productToDto(products);

        log.info("Retrieved {} products", productDTOs.size());

        return productDTOs;
    }

    @SneakyThrows
    public Product editProduct(String token, ProductDTO productDTO) {
        log.info("Editing product");

        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN) {
            log.error("Permission denied: You can't edit product");
            throw new NoPermissionException("You can't edit product");
        }
        Product product = productMapper.INSTANCE.dtoToProduct(productDTO);
        Optional<Product> productOptional = productRepository.findById(product.getId());
        Product productToUpdate = productOptional.orElseThrow(() -> new Exception("Product not found"));

        if (!productToUpdate.getName().equals(product.getName()) && productRepository.existsByNameAndCompanyId(product.getName(),jwtToken.getCompanyIdFromToken(token))) {
            log.error("Data exist: You need unique name for product");
            throw new DataExistException("You need unique name for product");
        }

        product.setAuditData(productToUpdate.getAuditData());
        product.getAuditData().setUpdateDate(LocalDate.now());
        product.setCompanyId(productToUpdate.getCompanyId());
        product.setProductCategoryId(productCategoryService.getProductCategoryByName(token, productDTO.getProductCategoryName()));
        productToUpdate.getAuditData().setUpdateDate(LocalDate.now());
        productRepository.save(product);

        log.info("Product edited successfully");

        return productToUpdate;
    }

    @SneakyThrows
    public void deleteProduct(String token, String id) {
        log.info("Deleting product");

        if (jwtToken.getRoleIdFromToken(token) != RoleOptions.ADMIN) {
            log.error("Permission denied: You can't delete products");
            throw new NoPermissionException("You can't delete products");
        }

        if (!productRepository.existsById(id)) {
            log.error("Data not found: Product not found");
            throw new Exception("Product not found");
        }

        productRepository.deleteById(id);

        log.info("Product deleted successfully");
    }
}
