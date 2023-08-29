package com.sapred.ordermanagerred.controller;

import com.sapred.ordermanagerred.dto.ProductDTO;
import com.sapred.ordermanagerred.dto.ProductNameDTO;
import com.sapred.ordermanagerred.model.Product;
import com.sapred.ordermanagerred.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/product")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@RequestHeader("token") String token, @RequestBody Product product) {
        log.debug("Entering addProduct method. Request body: {}", product);
        Product newProduct = productService.addProduct(token, product);
        return ResponseEntity.status(HttpStatus.OK).body(newProduct);
    }

    @GetMapping("/names/{prefix}")
    public ResponseEntity getAllNamesProducts(@RequestHeader("token") String token, @PathVariable("prefix") String prefix) {
        log.debug("Entering getAllNamesProducts method. PathVariable prefix: {}", prefix);
        List<ProductNameDTO> listProduct = productService.getAllNamesProducts(token, prefix);
        return ResponseEntity.status(HttpStatus.OK).body(listProduct);
    }

    @GetMapping
    public ResponseEntity getAllProduct(@RequestHeader("token") String token) {
        log.debug("Entering getAllProduct method");
        List<ProductDTO> products = productService.getAllProducts(token);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PutMapping
    public ResponseEntity editProduct(@RequestHeader("token") String token, @RequestBody ProductDTO productDTO) {
        log.debug("Entering editProduct method. Request body: {}", productDTO);
        Product productEdit = productService.editProduct(token, productDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@RequestHeader("token") String token, @PathVariable("id") String id) {
        log.debug("Entering deleteProduct method. PathVariable id: {}", id);
        productService.deleteProduct(token, id);
        return ResponseEntity.status(HttpStatus.OK).body("true");
    }
}
