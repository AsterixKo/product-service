package com.example.product_service.controllers;

import com.example.product_service.exceptions.ErrorSavingProductException;
import com.example.product_service.exceptions.ProductNotFoundException;
import com.example.product_service.models.entities.Product;
import com.example.product_service.models.entities.dtos.SaleDTO;
import com.example.product_service.models.enums.Category;
import com.example.product_service.services.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ProductControllerMockedTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("guardar producto")
    public void saveProductTest() throws Exception {
        Product productToSave = Product.builder()
                .name("Raton asus")
                .stock(100)
                .price(200.0)
                .description("Es un ratón cómodo")
                .category(Category.A)
                .build();

        Product productToReturn = Product.builder()
                .id(1L)
                .name("Raton asus")
                .stock(100)
                .price(200.0)
                .description("Es un ratón cómodo")
                .category(Category.A)
                .build();

        when(productService.saveProduct(productToSave)).thenReturn(productToReturn);


        String saveProductJson = objectMapper.writeValueAsString(productToSave);

        MvcResult result = mockMvc.perform(post("/api/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saveProductJson))
                .andExpect(status().isOk()).andReturn();

        String stringResponse = result.getResponse().getContentAsString();
        assertTrue(stringResponse.contains("Raton asus"));
    }

    @Test
    @DisplayName("buscar producto por id")
    public void findProductById() throws Exception {
        Product productToReturn = Product.builder()
                .id(1L)
                .name("Raton asus")
                .stock(100)
                .price(200.0)
                .description("Es un ratón cómodo")
                .category(Category.A)
                .build();

        when(productService.findProductById(1L)).thenReturn(productToReturn);

        MvcResult result = mockMvc.perform(get("/api/product/"+ 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L)).andReturn();

        String stringResponse = result.getResponse().getContentAsString();
        assertTrue(stringResponse.contains("Raton asus"));
    }

    @Test
    @DisplayName("actualizar stock producto")
    public void updateStockProduct() throws Exception {
        Product productToReturn = Product.builder()
                .id(1L)
                .name("Raton asus")
                .stock(90)
                .price(200.0)
                .description("Es un ratón cómodo")
                .category(Category.A)
                .build();

        when(productService.updateStockProduct(1L, 10)).thenReturn(productToReturn);

        SaleDTO saleDTO = SaleDTO.builder()
                .sale(10)
                .build();

        String saleDTOJson = objectMapper.writeValueAsString(saleDTO);

        MvcResult result = mockMvc.perform(put("/api/product/update/stock/"+ 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(saleDTOJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.stock").value(90)).andReturn();

        String stringResponse = result.getResponse().getContentAsString();
        assertTrue(stringResponse.contains("Raton asus"));
    }
}
