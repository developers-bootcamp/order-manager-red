package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@RestController
@CrossOrigin("http://localhost:3000")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@RequestHeader("token") String token, @RequestBody Product product) {
        try {
            Product newProduct = productService.addProduct(token, product);
            return ResponseEntity.ok().body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
//        catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
    }

    @GetMapping("/names/{prefix}")
    public ResponseEntity getAllNamesProducts(@RequestHeader("token") String token, @PathVariable("prefix") String prefix) {
        try {
            List<ProductNameDTO> listProduct = productService.getAllNamesProducts(token,prefix);
            return ResponseEntity.status(HttpStatus.OK).body(listProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getAllProduct(@RequestHeader("token") String token) {
        try {
            List<ProductDTO> products = productService.getAllProducts(token);
            return ResponseEntity.ok().body(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity editProduct(@RequestBody Product product) {
        try {
            Product productEdit = productService.editProduct(product);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/:id")
    public HttpStatus deleteProduct(@PathVariable("id") String id) {
        return productService.deleteProduct(id);
    }

}
//{
//        "name":"chocklate",
//        "desc":"milk 1",
//        "categoryId":"4",
//        "inventory":121,
//        "price":8,
//        "discountType":1
//}