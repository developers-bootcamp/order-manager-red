package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/ProductCategory")

public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/fill")
    public void fill() {
        productCategoryService.fill();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.createProductCategory(productCategory);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteProductCategory(@RequestHeader("token") String token, @PathVariable("id") String id) {
        return productCategoryService.deleteProductCategory(token, id);
    }

   @GetMapping("/getAllCategory")
   public List<ProductCategoryDto> getAllCategory(@RequestHeader("token") String token) {
       return productCategoryService.getAllCategory(token);
   }

}
