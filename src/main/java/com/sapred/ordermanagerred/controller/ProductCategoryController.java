package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.DataExistException;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProductCategory")
@CrossOrigin("http://localhost:3000")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProductCategory(@RequestHeader("token") String token, @PathVariable("id") String id) {
        try {
            productCategoryService.deleteProductCategory(token, id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NoPermissionException exception) {
            return new ResponseEntity(exception, HttpStatus.FORBIDDEN);
        } catch (Exception exception) {
            return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<ProductCategoryDto>> getAllCategory(@RequestHeader("token") String token) {
        try {
            List<ProductCategoryDto> productCategoryDtos = productCategoryService.getAllCategory(token);
            return new ResponseEntity<>(productCategoryDtos, HttpStatus.OK);
        } catch (NoPermissionException exception) {
            return new ResponseEntity(exception, HttpStatus.FORBIDDEN);
        } catch (Exception exception) {
            return new ResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping()
    public ResponseEntity createProductCategory(@RequestHeader String token, @RequestBody ProductCategory productCategory) {
        try {
            productCategoryService.createProductCategory(productCategory, token);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NoPermissionException ex) {
            return new ResponseEntity(ex, HttpStatus.FORBIDDEN);
        } catch (DataExistException ex) {
            return new ResponseEntity(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


