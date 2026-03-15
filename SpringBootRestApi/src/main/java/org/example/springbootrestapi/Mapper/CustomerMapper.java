package org.example.springbootrestapi.Mapper;

import org.example.springbootrestapi.DTO.Request.CustomerRequestDTO;
import org.example.springbootrestapi.DTO.Request.CustomerUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.CustomerResponseDTO;
import org.example.springbootrestapi.Model.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        if (dto == null) return null;

        return new Customer(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                dto.getAddress()
        );
    }

    public Customer toEntityUpdate(CustomerUpdateRequestDTO dto) {
        if (dto == null) return null;

        return new Customer(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getAge(),
                dto.getAddress()
        );
    }


    public CustomerResponseDTO toDto(Customer customer) {
        if (customer == null) return null;

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }

    public List<CustomerResponseDTO> toDtoList(Map<String, Customer> customers) {
        return customers
                .values()
                .stream()
                .map(this::toDto)
                .toList();
    }
}