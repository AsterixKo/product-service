package com.example.product_service.services;

import com.example.product_service.exceptions.ErrorSavingProductException;
import com.example.product_service.exceptions.ProductNotFoundException;
import com.example.product_service.models.entities.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public void deleteProductById(Long idProduct) {
        productRepository.deleteById(idProduct);
    }

    public Product findProductById(Long idProduct) {
        Optional<Product> productOptional = productRepository.findById(idProduct);

        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            return null;
        }
    }

    public Product saveProduct(Product product) throws ErrorSavingProductException {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            log.error("Error guardando product {}", e.getMessage());
            throw new ErrorSavingProductException("Error guardando product");
        }
    }

    public List<Product> saveListProducts(List<Product> products) throws ErrorSavingProductException {
        try {
            return productRepository.saveAll(products);
        } catch (Exception e) {
            log.error("Error guardando product {}", e.getMessage());
            throw new ErrorSavingProductException("Error guardando product");
        }
    }

    public Product updateStockProduct(Long idProduct, Integer sale) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(idProduct);
        if (product.isPresent()) {
            Integer stock = product.get().getStock();
            product.get().setStock(stock - sale);
            log.info("Guardando product");
            return productRepository.save(product.get());
        } else {
            log.error("Error product not found");
            throw new ProductNotFoundException("Error product not found");
        }
    }
}
