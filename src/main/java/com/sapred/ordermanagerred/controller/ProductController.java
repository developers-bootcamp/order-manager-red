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
    public ResponseEntity addProduct(@RequestBody Product product) {
        try {
            Product newProduct = productService.addProduct(product);
            return ResponseEntity.ok().body(newProduct);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
//        catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
    }

    @GetMapping("/names")
    public HttpStatus getAllNamesProducts(){
        return  productService.getAllNamesProducts();
    }

    @GetMapping
    public ResponseEntity getAllProduct(){
        return  productService.getAllProducts();
    }

    @PutMapping
    public HttpStatus editProduct(@RequestBody Product product){return productService.editProduct(product);}
    @DeleteMapping("/:id")
    public HttpStatus deleteProduct(@PathVariable("id") String id){return productService.deleteProduct(id);}

}
