package org.example.springbootrestapi.Controller;

import jakarta.validation.Valid;
import org.example.springbootrestapi.DTO.Request.ProductRequestDTO;
import org.example.springbootrestapi.DTO.Request.ProductUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.ProductResponseDTO;
import org.example.springbootrestapi.Service.ProductService;
import org.example.springbootrestapi.Exception.MissingApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private static final String API_KEY = "123456";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private void checkApiKey(String apiKey) {
        if (apiKey == null || !API_KEY.equals(apiKey)) {
            throw new MissingApiKeyException("Invalid or missing API-Key");
        }
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestHeader("API-Key") String apiKey,
                                                     @Valid @RequestBody ProductRequestDTO requestDTO) {
        checkApiKey(apiKey);
        ProductResponseDTO created = productService.addProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll(@RequestHeader("API-Key") String apiKey) {
        checkApiKey(apiKey);
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ProductResponseDTO> getByName(@RequestHeader("API-Key") String apiKey,
                                                        @PathVariable String name) {
        checkApiKey(apiKey);
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ProductResponseDTO> update(@RequestHeader("API-Key") String apiKey,
                                                     @PathVariable String name,
                                                     @Valid @RequestBody ProductUpdateRequestDTO updateDTO) {
        checkApiKey(apiKey);
        ProductResponseDTO updated = productService.updateProduct(name, updateDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@RequestHeader("API-Key") String apiKey,
                                       @PathVariable String name) {
        checkApiKey(apiKey);
        productService.deleteProduct(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getByCategory(@RequestHeader("API-Key") String apiKey,
                                                                  @PathVariable String category) {
        checkApiKey(apiKey);
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductResponseDTO>> getByPriceRange(@RequestHeader("API-Key") String apiKey,
                                                                    @RequestParam double min,
                                                                    @RequestParam double max) {
        checkApiKey(apiKey);
        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max));
    }
}