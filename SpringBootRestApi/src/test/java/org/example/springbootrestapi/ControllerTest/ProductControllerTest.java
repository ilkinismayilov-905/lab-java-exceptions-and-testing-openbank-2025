package org.example.springbootrestapi.ControllerTest;

import org.example.springbootrestapi.Controller.ProductController;
import org.example.springbootrestapi.DTO.Request.ProductRequestDTO;
import org.example.springbootrestapi.DTO.Request.ProductUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.ProductResponseDTO;
import org.example.springbootrestapi.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private static final String VALID_API_KEY = "123456";

    // ProductResponseDTO üçün helper (Constructor ardıcıllığına diqqət!)
    private ProductResponseDTO createProductResponse(String name, double price, String category, int quantity) {
        return new ProductResponseDTO(name, price, category, quantity, "In Stock");
    }

    @Test
    void getAll_withValidKey_returnsOk() throws Exception {
        ProductResponseDTO p1 = createProductResponse("Laptop", 1500.0, "Electronics", 5);
        when(productService.getAllProducts()).thenReturn(List.of(p1));

        mockMvc.perform(get("/api/products")
                        .header("API-Key", VALID_API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].status").value("In Stock"));
    }

    @Test
    void create_validRequest_returns201() throws Exception {
        ProductResponseDTO response = createProductResponse("Phone", 800.0, "Electronics", 10);

        when(productService.addProduct(any(ProductRequestDTO.class))).thenReturn(response);

        String requestBody = """
                {
                    "id": 1,
                    "name": "Phone",
                    "price": 800.0,
                    "category": "Electronics",
                    "quantity": 10
                }
                """;

        mockMvc.perform(post("/api/products")
                        .header("API-Key", VALID_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    void update_validRequest_returnsOk() throws Exception {
        ProductResponseDTO response = createProductResponse("Updated Laptop", 1400.0, "Electronics", 3);

        when(productService.updateProduct(anyString(), any(ProductUpdateRequestDTO.class)))
                .thenReturn(response);

        String requestBody = """
                {
                    "name": "Laptop",
                    "price": 1400.0,
                    "quantity": 3
                }
                """;

        mockMvc.perform(put("/api/products/Laptop")
                        .header("API-Key", VALID_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"));
    }

    @Test
    void getByPriceRange_returnsList() throws Exception {
        ProductResponseDTO p = createProductResponse("Cheap Item", 15.0, "Misc", 100);
        when(productService.getProductsByPriceRange(10.0, 20.0)).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products/price")
                        .header("API-Key", VALID_API_KEY)
                        .param("min", "10.0")
                        .param("max", "20.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}