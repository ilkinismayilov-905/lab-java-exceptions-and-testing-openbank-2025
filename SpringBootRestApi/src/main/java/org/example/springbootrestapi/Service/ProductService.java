package org.example.springbootrestapi.Service;

import org.example.springbootrestapi.DTO.Request.ProductRequestDTO;
import org.example.springbootrestapi.DTO.Request.ProductUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.ProductResponseDTO;
import org.example.springbootrestapi.Exception.ProductNotFoundException;
import org.example.springbootrestapi.Mapper.CustomerMapper;
import org.example.springbootrestapi.Mapper.ProductMapper;
import org.example.springbootrestapi.Model.Product;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    private Map<String, Product> productMap = new HashMap<>();

    public ProductResponseDTO addProduct(ProductRequestDTO requestDTO) {

        Product product = productMapper.toEntity(requestDTO);
        productMap.put(product.getName(), product);


        return productMapper.toDto(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productMapper.toDto(productMap);
    }

    public ProductResponseDTO getProductByName(String name) {
        Product product = productMap.get(name);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        return productMapper.toDto(product);
    }

    public ProductResponseDTO updateProduct(String name, ProductUpdateRequestDTO updateDTO) {
        Product existingProduct = productMap.get(name);
        if (existingProduct == null) {
            throw new ProductNotFoundException("Product not found: " + name);
        }

        // Sahələri null yoxlaması ilə yeniləyirik
        if (updateDTO.getPrice() != null) existingProduct.setPrice(updateDTO.getPrice());
        if (updateDTO.getCategory() != null) existingProduct.setCategory(updateDTO.getCategory());
        if (updateDTO.getQuantity() != null) existingProduct.setQuantity(updateDTO.getQuantity());

        productMap.put(name, existingProduct);
        return productMapper.toDto(existingProduct);
    }

    public void deleteProduct(String name) {
        if (!productMap.containsKey(name)) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        productMap.remove(name);
    }

    public List<ProductResponseDTO> getProductsByCategory(String category) {
        return productMap.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .map(productMapper::toDto)
                .toList();
    }

    public List<ProductResponseDTO> getProductsByPriceRange(double min, double max) {
        return productMap.values().stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .map(productMapper::toDto)
                .toList();
    }
}