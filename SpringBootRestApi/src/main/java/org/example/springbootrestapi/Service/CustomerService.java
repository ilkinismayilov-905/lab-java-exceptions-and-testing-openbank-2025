package org.example.springbootrestapi.Service;

import org.example.springbootrestapi.DTO.Request.CustomerRequestDTO;
import org.example.springbootrestapi.DTO.Request.CustomerUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.CustomerResponseDTO;
import org.example.springbootrestapi.Mapper.CustomerMapper;
import org.example.springbootrestapi.Model.Customer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    private Map<String, Customer> customers = new HashMap<>();

    public CustomerResponseDTO save(CustomerRequestDTO requestDTO) {
        Customer customer = customerMapper.toEntity(requestDTO);

        customers.put(customer.getEmail(), customer);
        return customerMapper.toDto(customer);
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerMapper.toDtoList(customers);
    }

    public CustomerResponseDTO getCustomerByEmail(String email) {
        Customer customer = customers.get(email);
        return (customer != null) ? customerMapper.toDto(customer) : null;
    }

    public CustomerResponseDTO updateCustomer(String email, CustomerUpdateRequestDTO updateDTO) {
        Customer existingCustomer = customers.get(email);
        if (existingCustomer != null) {
            if (updateDTO.getName() != null) existingCustomer.setName(updateDTO.getName());
            if (updateDTO.getAge() != null) existingCustomer.setAge(updateDTO.getAge());
            if (updateDTO.getAddress() != null) existingCustomer.setAddress(updateDTO.getAddress());

            customers.put(email, existingCustomer);
            return customerMapper.toDto(existingCustomer);
        }
        return null;
    }

    public void deleteCustomer(String email) {
        customers.remove(email);
    }

}