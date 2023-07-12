package com.sapred.ordermanagerred.service;

import com.sapred.ordermanagerred.model.AuditData;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public HttpStatus addProduct(Product product){
        if (productRepository.existsByName(product.getName())==true)
            return HttpStatus.CONFLICT;
        product.setAuditData(new AuditData(new Date(),null));
        Product newProduct= productRepository.insert(product);
        if(newProduct==null)
            return HttpStatus.INTERNAL_SERVER_ERROR;
        return  HttpStatus.OK;
    }

    public HttpStatus getAllNamesProducts(){
         List<Product> products = productRepository.findAll().stream().toList();
        if(products==null)
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

    public HttpStatus getAllProducts(){
        List<Product> products = productRepository.findAll().stream().toList();
        if(products==null)
            return HttpStatus.INTERNAL_SERVER_ERROR;

        List<Map<String, Object>> productList = products.stream()
                .map(item -> {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", item.getId());
                    productMap.put("name", item.getName());
                    productMap.put("desc", item.getDesc());
                    productMap.put("productCategoryId", item.getProductCategoryId());
                    productMap.put("inventory", item.getInventory());
                    productMap.put("price", item.getPrice());

                    return productMap;
                })
                .collect(Collectors.toList());

        return HttpStatus.OK;
    }

    public  HttpStatus editProduct(Product product){
        product.getAuditData().setUpdateDate(new Date());
        productRepository.save(product);
        return HttpStatus.OK;
    }
    public  HttpStatus deleteProduct(String id){
        if (productRepository.existsById(id))
            return HttpStatus.NOT_FOUND;
        productRepository.deleteById(id);
        return  HttpStatus.OK;
    }
}
