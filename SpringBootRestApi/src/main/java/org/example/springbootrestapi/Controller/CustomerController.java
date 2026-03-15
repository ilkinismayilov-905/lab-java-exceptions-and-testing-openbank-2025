package org.example.springbootrestapi.Controller;

import jakarta.validation.Valid;
import org.example.springbootrestapi.DTO.Request.CustomerRequestDTO;
import org.example.springbootrestapi.DTO.Request.CustomerUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.CustomerResponseDTO;
import org.example.springbootrestapi.Exception.CustomerNotFoundException;
import org.example.springbootrestapi.Service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO saved = customerService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAll() {
        List<CustomerResponseDTO> list = customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomerResponseDTO> getByEmail(@PathVariable String email) {
        CustomerResponseDTO responseDTO = customerService.getCustomerByEmail(email);
        if (responseDTO == null) {
            throw new CustomerNotFoundException("Customer with email " + email + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


    @PutMapping("/{email}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable String email,
                                                      @Valid @RequestBody CustomerUpdateRequestDTO updateDTO) {
        CustomerResponseDTO updated = customerService.updateCustomer(email, updateDTO);
        if (updated == null) {
            throw new CustomerNotFoundException("Customer with email " + email + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        customerService.deleteCustomer(email);
        return ResponseEntity.noContent().build();
    }
}