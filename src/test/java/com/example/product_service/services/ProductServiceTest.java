package com.example.product_service.services;

import com.example.product_service.exceptions.ErrorSavingProductException;
import com.example.product_service.exceptions.ProductNotFoundException;
import com.example.product_service.models.entities.Product;
import com.example.product_service.models.enums.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("guardar producto")
    public void saveProductTest() throws ErrorSavingProductException {

        assertThrows(ErrorSavingProductException.class,
                () -> productService.saveProduct(
                        Product.builder()
                                .name(null)
                                .description("test_description1")
                                .price(200.0)
                                .category(Category.A)
                                .stock(100)
                                .build()));

        assertThrows(ErrorSavingProductException.class,
                () -> productService.saveProduct(
                        Product.builder()
                                .name("test_name2")
                                .description("test_description2")
                                .price(null)
                                .category(Category.A)
                                .stock(100)
                                .build()));

        assertThrows(ErrorSavingProductException.class,
                () -> productService.saveProduct(
                        Product.builder()
                                .name("test_name3")
                                .description("test_description3")
                                .price(200.0)
                                .category(null)
                                .stock(100)
                                .build()));

        assertThrows(ErrorSavingProductException.class,
                () -> productService.saveProduct(
                        Product.builder()
                                .name("test_name4")
                                .description("test_description4")
                                .price(200.0)
                                .category(Category.A)
                                .stock(null)
                                .build()));

        Product product5 = productService.saveProduct(
                Product.builder()
                        .name("test_name5")
                        .description("test_description5")
                        .price(200.0)
                        .category(Category.A)
                        .stock(100)
                        .build());

        assertEquals("test_name5", product5.getName());
        assertEquals("test_description5", product5.getDescription());
        assertEquals(200.0, product5.getPrice());
        assertEquals(Category.A, product5.getCategory());
        assertEquals(100, product5.getStock());

        // no puede haber 2 con el mismo nombre
        // no funciona la constraint unique en jpa al crear la base de datos con column de tipo TEXT
        // habria que hacer la constraint a mano en base de datos porque si no podemos tener products duplicados
//        assertThrows(ErrorSavingProductException.class,
//                () -> productService.saveProduct(
//                        Product.builder()
//                                .name("test_name5")
//                                .description("test_description5")
//                                .price(200.0)
//                                .category(Category.A)
//                                .stock(100)
//                                .build()));
    }

    @Test
    @DisplayName("busca producto por id")
    public void findProductById() throws ErrorSavingProductException {
        Product product = Product.builder()
                .name("product_test_teclado")
                .description("teclado con cable")
                .category(Category.A)
                .price(200.0)
                .stock(100)
                .build();

        Product productSaved = productService.saveProduct(product);

        Product productFound = productService.findProductById(productSaved.getId());

        assertNotNull(productFound);
        assertEquals("product_test_teclado", productFound.getName());
        assertEquals("teclado con cable", productFound.getDescription());
        assertEquals(Category.A, productFound.getCategory());
        assertEquals(200.0, productFound.getPrice());
        assertEquals(100, productFound.getStock());
    }

    @Test
    @DisplayName("delete product por id")
    public void deleteProductById() throws ErrorSavingProductException {
        Product product = Product.builder()
                .name("product_test_delete_raton")
                .description("raton con cable")
                .category(Category.A)
                .price(200.0)
                .stock(100)
                .build();

        Product productSaved = productService.saveProduct(product);

        productService.deleteProductById(productSaved.getId());

        Product productFound = productService.findProductById(productSaved.getId());

        assertNull(productFound);
    }

    @Test
    @DisplayName("actualizar stock de producto")
    public void updateStockProduct() throws ErrorSavingProductException, ProductNotFoundException {
        Product product = Product.builder()
                .name("product_test_update_stock_monitor")
                .description("Monitor MSI")
                .category(Category.A)
                .price(200.0)
                .stock(100)
                .build();

        Product productSaved = productService.saveProduct(product);

        Product productUpdated = productService.updateStockProduct(productSaved.getId(),1);

        assertEquals(99, productUpdated.getStock());
    }
}
