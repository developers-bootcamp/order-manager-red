package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.Exception.ObjectDoesNotExistException;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
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
        log.debug("Entering deleteProductCategory method");
        log.debug("@PathVariable id: {}", id);

        productCategoryService.deleteProductCategory(token, id);

        log.debug("Exiting deleteProductCategory method");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductCategoryDto>> getAllCategory(@RequestHeader("token") String token) {
        log.debug("Entering getAllCategory method");

        List<ProductCategoryDto> productCategoryDtos = productCategoryService.getAllCategory(token);

        log.debug("Exiting getAllCategory method");
        return ResponseEntity.ok(productCategoryDtos);
    }

    @PostMapping
    public ResponseEntity createProductCategory(@RequestHeader String token, @RequestBody ProductCategory productCategory) {
        log.debug("Entering createProductCategory method");
        log.debug("@RequestBody productCategory: {}", productCategory);

        productCategoryService.createProductCategory(productCategory, token);

        log.debug("Exiting createProductCategory method");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> editProductCategory(@RequestHeader("token") String token, @RequestBody ProductCategoryDto productCategoryDto) {
        log.debug("Entering editProductCategory method");
        log.debug("@RequestBody productCategoryDto: {}", productCategoryDto);

        productCategoryService.editProductCategory(token, productCategoryDto);

        log.debug("Exiting editProductCategory method");
        return new ResponseEntity(HttpStatus.OK);
    }
}
