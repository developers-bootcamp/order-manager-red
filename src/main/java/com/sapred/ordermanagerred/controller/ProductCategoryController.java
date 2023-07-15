package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.Mapper.ProductCategoryMapper;
import com.sapred.ordermanagerred.Mapper.ProductCategoryMapperImpl;
import com.sapred.ordermanagerred.exception.ObjectDoesNotExistException;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sapred.ordermanagerred.Mapper.ProductCategoryMapperImpl.*;

import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/ProductCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;


    @PostMapping("/create")
    public ResponseEntity<String> createProductCategory(@RequestBody ProductCategory productCategory) {
        return productCategoryService.createProductCategory(productCategory);
    }

    @GetMapping("/fill")
    public void fill() {
        productCategoryService.fill();
    }

    @PutMapping()
    public ResponseEntity<String> editProductCategory(@RequestHeader("token") String token, @RequestBody ProductCategoryDto productCategoryDto) {
        try {
            productCategoryService.editProductCategory(token, productCategoryDto);
        } catch (NoPermissionException ex) {
            return new ResponseEntity(ex, HttpStatus.FORBIDDEN);
        } catch (ObjectDoesNotExistException ex) {
            return new ResponseEntity(ex, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            System.out.println("Exception ex Exception ex");
            System.out.println(ex);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(HttpStatus.OK);
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
}

