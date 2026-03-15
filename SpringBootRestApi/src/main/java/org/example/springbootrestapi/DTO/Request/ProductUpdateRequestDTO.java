package org.example.springbootrestapi.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public class ProductUpdateRequestDTO {

    @NotBlank(message = "Product name is required to identify the update")
    @Size(min = 3)
    private String name;

    @Positive(message = "Price must be a positive number")
    private Double price;

    private String category;

    @Positive(message = "Quantity must be a positive number")
    private Integer quantity;

    public ProductUpdateRequestDTO(String name, Double price, String category, Integer quantity) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public ProductUpdateRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}