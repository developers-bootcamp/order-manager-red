package com.sapred.ordermanagerred.service;

import com.mongodb.client.model.ReturnDocument;
import com.sapred.ordermanagerred.dto.productMapper;
import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.repository.AuditDataRepository;
import com.sapred.ordermanagerred.repository.ProductRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuditDataRepository auditDataRepository;

    @SneakyThrows
    public Product addProduct(Product product) {
        if (productRepository.existsByName(product.getName()) == true)
            throw new Exception();
        product.setAuditData(new AuditData(new Date(), null));
        return productRepository.insert(product);
    }

    public HttpStatus getAllNamesProducts() {
        List<Product> products = productRepository.findAll().stream().toList();
        if (products == null)
            return HttpStatus.INTERNAL_SERVER_ERROR;
        List<Map<String, Object>> productList = products.stream()
                .map(item -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", item.getId());
                    productMap.put("name", item.getName());
                    return productMap;
                })
                .collect(Collectors.toList());
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ResponseEntity getAllProducts() {
        List<Product> products = productRepository.findAll().stream().toList();
        if (products == null)
            return ResponseEntity.status(207).body("mmmm");
            //return HttpStatus.INTERNAL_SERVER_ERROR;
        List<ProductDTO> productDTOs = productMapper.INSTANCE.productToDto(products);
        //return HttpStatus.OK;
        return ResponseEntity.ok(productDTOs);
    }

    public HttpStatus editProduct(Product product) {
        product.getAuditData().setUpdateDate(new Date());
        productRepository.save(product);
        return HttpStatus.OK;
    }

    public HttpStatus deleteProduct(String id) {
        if (productRepository.existsById(id) != true)
            return HttpStatus.NOT_FOUND;
        productRepository.deleteById(id);
        return HttpStatus.OK;
    }
}
