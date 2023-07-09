package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.Exception.AccessPermissionException;
import com.sapred.ordermanagerred.Exception.DataExistException;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ProductCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/create")
    public ResponseEntity createProductCategory(@RequestHeader String token, @RequestBody ProductCategory productCategory) {
        try {
            productCategoryService.createProductCategory(productCategory, token);
            return new ResponseEntity(HttpStatus.OK);
        } catch (AccessPermissionException ex) {
            return new ResponseEntity(ex, HttpStatus.FORBIDDEN);
        } catch (DataExistException ex) {
            return new ResponseEntity(ex, HttpStatus.CONFLICT);
        } catch (Exception ex) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
