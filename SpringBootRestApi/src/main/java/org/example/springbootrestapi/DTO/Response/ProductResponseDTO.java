package org.example.springbootrestapi.DTO.Response;

import lombok.Builder;

@Builder
public class ProductResponseDTO {
    private String name;
    private double price;
    private String category;
    private int quantity;
    private String status;

    public ProductResponseDTO(String name, double price, String category, int quantity, String status) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.status = status;
    }

    public ProductResponseDTO() {
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}