package org.example.springbootrestapi.ControllerTest;

import org.example.springbootrestapi.Controller.CustomerController;
import org.example.springbootrestapi.DTO.Request.CustomerRequestDTO;
import org.example.springbootrestapi.DTO.Request.CustomerUpdateRequestDTO;
import org.example.springbootrestapi.DTO.Response.CustomerResponseDTO;
import org.example.springbootrestapi.Exception.CustomerNotFoundException;
import org.example.springbootrestapi.Service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;


    private CustomerResponseDTO createResponseDTO(Long id, String name, String email) {
        return new CustomerResponseDTO(id, name, email);
    }

    @Test
    void getAll_returnsOk() throws Exception {
        CustomerResponseDTO c1 = createResponseDTO(1L, "Ali", "ali@mail.com");
        CustomerResponseDTO c2 = createResponseDTO(2L, "Vali", "vali@mail.com");

        when(customerService.getAllCustomers()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ali"));
    }

    @Test
    void getByEmail_existingEmail_returnsCustomer() throws Exception {
        CustomerResponseDTO response = createResponseDTO(1L, "Ali", "ali@mail.com");

        when(customerService.getCustomerByEmail("ali@mail.com")).thenReturn(response);

        mockMvc.perform(get("/api/customers/ali@mail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("ali@mail.com"));
    }

    @Test
    void create_validRequest_returns201() throws Exception {
        CustomerResponseDTO savedResponse = createResponseDTO(10L, "Yeni Musteri", "new@mail.com");

        // Service artıq CustomerRequestDTO qəbul edir
        when(customerService.save(any(CustomerRequestDTO.class))).thenReturn(savedResponse);

        String requestBody = """
                {
                    "id": 10,
                    "name": "Yeni Musteri",
                    "email": "new@mail.com",
                    "age": 20,
                    "address": "Sumqayit"
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Yeni Musteri"));
    }

    @Test
    void update_validRequest_returnsUpdatedCustomer() throws Exception {
        CustomerResponseDTO updatedResponse = createResponseDTO(1L, "Ali Updated", "ali@mail.com");

        when(customerService.updateCustomer(anyString(), any(CustomerUpdateRequestDTO.class)))
                .thenReturn(updatedResponse);

        String requestBody = """
                {
                    "id": 1,
                    "name": "Ali Updated",
                    "age": 26,
                    "address": "Baku Updated"
                }
                """;

        mockMvc.perform(put("/api/customers/ali@mail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ali Updated"));
    }

    @Test
    void getByEmail_nonExistentEmail_returns404() throws Exception {
        when(customerService.getCustomerByEmail("t123@mail.com"))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/t123@mail.com"))
                .andExpect(status().isNotFound());
    }
}