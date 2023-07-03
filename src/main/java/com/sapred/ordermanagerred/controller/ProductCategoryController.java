package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ProductCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/create")
    public ResponseEntity<String> createProductCategory(@RequestHeader String token, @RequestBody ProductCategory productCategory){
        return productCategoryService.createProductCategory(productCategory, token);
    }
}
