package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public HttpStatus addProduct(@RequestBody Product product){
        return  productService.addProduct(product);
    }

    @GetMapping("/names")
    public HttpStatus getAllNamesProducts(){
        return  productService.getAllNamesProducts();
    }

    @GetMapping
    public HttpStatus getAllProduct(){
        return  productService.getAllProducts();
    }

    @PutMapping
    public HttpStatus editProduct(@RequestBody Product product){return productService.editProduct(product);}
    @DeleteMapping("/:id")
    public HttpStatus deleteProduct(@PathVariable("id") String id){return productService.deleteProduct(id);}

}
