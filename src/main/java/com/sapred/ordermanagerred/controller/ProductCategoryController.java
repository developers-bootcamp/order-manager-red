package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productCategory")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductCategory(@RequestHeader("token") String token, @PathVariable("id") String id) {
        log.debug("Entering deleteProductCategory method. @PathVariable id: {}", id);
        productCategoryService.deleteProductCategory(token, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getAllCategory(@RequestHeader("token") String token) {
        log.debug("Entering getAllCategory method");
        List<ProductCategoryDto> productCategoryDtos = productCategoryService.getAllCategory(token);
        return ResponseEntity.ok(productCategoryDtos);
    }

    @PostMapping
    public ResponseEntity createProductCategory(@RequestHeader String token, @RequestBody ProductCategory productCategory) {
        log.debug("Entering createProductCategory method. @RequestBody productCategory: {}", productCategory);
        productCategoryService.createProductCategory(productCategory, token);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> editProductCategory(@RequestHeader("token") String token, @RequestBody ProductCategoryDto productCategoryDto) {
        log.debug("Entering editProductCategory method. @RequestBody productCategoryDto: {}", productCategoryDto);
        productCategoryService.editProductCategory(token, productCategoryDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}

