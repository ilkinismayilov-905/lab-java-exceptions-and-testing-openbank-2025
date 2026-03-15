package org.example.springbootrestapi.Mapper;

import org.example.springbootrestapi.DTO.Request.ProductRequestDTO;
import org.example.springbootrestapi.DTO.Response.ProductResponseDTO;
import org.example.springbootrestapi.Model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        if (dto == null) return null;

        return new Product(
                dto.getName(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getQuantity()
        );
    }

    public ProductResponseDTO toDto(Product product) {
        if (product == null) return null;

        String status = (product.getQuantity() > 0) ? "In Stock" : "Out of Stock";

        return new ProductResponseDTO(
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity(),
                status
        );
    }

    public List<ProductResponseDTO> toDto(Map<String, Product> products) {
        return products
                .values()
                .stream()
                .map(this::toDto)
                .toList();
    }
}