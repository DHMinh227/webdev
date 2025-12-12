package com.example.customerapi.service;

import com.example.customerapi.dto.CustomerRequestDTO;
import com.example.customerapi.dto.CustomerResponseDTO;
import java.util.List;

public interface CustomerService {

    List<CustomerResponseDTO> getAllCustomers();
    List<CustomerResponseDTO> searchCustomers(String keyword);
    List<CustomerResponseDTO> getCustomersByStatus(String status);
    List<CustomerResponseDTO> advancedSearch(String name, String email, String status);
    List<CustomerResponseDTO> getAllCustomers(Sort sort);

    Page<CustomerResponseDTO> getAllCustomers(int page, int size);

    CustomerResponseDTO getCustomerById(Long id);

    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);

    void deleteCustomer(Long id);
}
