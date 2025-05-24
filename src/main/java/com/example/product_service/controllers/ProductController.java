package com.example.product_service.controllers;

import com.example.product_service.exceptions.ErrorSavingProductException;
import com.example.product_service.exceptions.ProductNotFoundException;
import com.example.product_service.models.entities.Product;
import com.example.product_service.models.entities.dtos.SaleDTO;
import com.example.product_service.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public ResponseEntity<?> findAllProducts() {
        try {
            return new ResponseEntity<>(productService.findAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error obteniendo products {}", e.getMessage());
            return new ResponseEntity<>("Error obteniendo products", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/stock/{idProduct}")
    public ResponseEntity<?> updateStockProduct(@PathVariable("idProduct") Long idProduct, @RequestBody SaleDTO saleDTO) {
        try {
            return new ResponseEntity<>(productService.updateStockProduct(idProduct, saleDTO.getSale()), HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            log.error("Error product not found {}", e.getMessage());
            return new ResponseEntity<>("Error product not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        try {
            return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.OK);
        } catch (ErrorSavingProductException e) {
            log.error("Error saving product {}", e.getMessage());
            return new ResponseEntity<>("Error saving product", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save/list")
    public ResponseEntity<?> saveListProduct(@RequestBody List<Product> products) {
        try {
            return new ResponseEntity<>(productService.saveListProducts(products), HttpStatus.OK);
        } catch (ErrorSavingProductException e) {
            log.error("Error saving product {}", e.getMessage());
            return new ResponseEntity<>("Error saving product", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{idProduct}")
    public ResponseEntity<?> findProductById(@PathVariable("idProduct") Long idProduct) {
        try {
            return new ResponseEntity<>(productService.findProductById(idProduct), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProduct(@PathVariable("idProduct") Long idProduct) {
        try {
            productService.deleteProductById(idProduct);
            return new ResponseEntity<>("product eliminado correctamente", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error inesperado {}", e.getMessage());
            return new ResponseEntity<>("Error inesperado", HttpStatus.BAD_REQUEST);
        }
    }
}
