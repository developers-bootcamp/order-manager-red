package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.exception.NoPremissionException;
import com.sapred.ordermanagerred.exception.ObjectDoesNotExistException;
import com.sapred.ordermanagerred.dto.ProductCategoryDto;
import com.sapred.ordermanagerred.exception.NoPermissionException;
import com.sapred.ordermanagerred.model.ProductCategory;
import com.sapred.ordermanagerred.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("http://localhost:3000")
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
    @PutMapping()
    public  ResponseEntity<String>  editProductCategory(@RequestHeader("token") String token,@RequestBody ProductCategory productCategory) {
        try {
             productCategoryService.editProductCategory(token, productCategory);

        } catch (NoPremissionException ex) {
            return new ResponseEntity(ex, HttpStatus.FORBIDDEN);
        }
        catch (ObjectDoesNotExistException ex) {
            return new ResponseEntity(ex, HttpStatus.NOT_FOUND);
        }
        catch (Exception ex) {
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

