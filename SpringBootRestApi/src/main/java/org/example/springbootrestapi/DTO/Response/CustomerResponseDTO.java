package org.example.springbootrestapi.DTO.Response;



public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String email;

    public CustomerResponseDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public CustomerResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
