package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")

@RestController
@RequestMapping("/ProductCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/create")
    public ResponseEntity<String> createProductCategory(@RequestBody ProductCategory productCategory){
        return productCategoryService.createProductCategory(productCategory);
    }
    @GetMapping("/fill")
    public void fill() {
        productCategoryService.fill();
    }
    @PutMapping
    public HttpStatus editProductCategory(@RequestHeader("token") String token,@RequestBody ProductCategory productCategory){
        return productCategoryService.editProductCategory(token,productCategory);
    }
}
