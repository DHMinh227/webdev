package com.example.customerapi.service;

import com.example.customerapi.dto.CustomerRequestDTO;
import com.example.customerapi.dto.CustomerResponseDTO;
import com.example.customerapi.entity.Customer;
import com.example.customerapi.entity.CustomerStatus;
import com.example.customerapi.exception.ResourceNotFoundException;
import com.example.customerapi.exception.DuplicateResourceException;
import com.example.customerapi.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + id)
                );
        return convertToResponseDTO(customer);
    }

    @Override
    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {

        if (customerRepository.existsByCustomerCode(dto.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + dto.getCustomerCode());
        }

        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }

        Customer customer = convertToEntity(dto);
        Customer saved = customerRepository.save(customer);

        return convertToResponseDTO(saved);
    }

    @Override
    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + id)
                );

        if (!existing.getEmail().equals(dto.getEmail())
                && customerRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + dto.getEmail());
        }

        existing.setCustomerCode(dto.getCustomerCode());
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setAddress(dto.getAddress());
        existing.setStatus(CustomerStatus.valueOf(dto.getStatus()));

        Customer updated = customerRepository.save(existing);
        return convertToResponseDTO(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with ID: " + id)
                );
        customerRepository.delete(existing);
    }

    private CustomerResponseDTO convertToResponseDTO(Customer c) {
        return new CustomerResponseDTO(
                c.getId(),
                c.getCustomerCode(),
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress(),
                c.getStatus().name(),
                c.getCreatedAt()
        );
    }

    private Customer convertToEntity(CustomerRequestDTO dto) {
        Customer c = new Customer();
        c.setCustomerCode(dto.getCustomerCode());
        c.setFullName(dto.getFullName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        c.setAddress(dto.getAddress());
        c.setStatus(CustomerStatus.valueOf(dto.getStatus()));
        return c;
    }

    @Override
public List<CustomerResponseDTO> searchCustomers(String keyword) {
    List<Customer> list = customerRepository.searchCustomers(keyword);
    return list.stream()
            .map(this::convertToResponseDTO)
            .toList();
}
@Override
public List<CustomerResponseDTO> getCustomersByStatus(String status) {
    List<Customer> list = customerRepository.findByStatus(status);
    return list.stream()
            .map(this::convertToResponseDTO)
            .toList();
}
@Override
public Page<CustomerResponseDTO> getAllCustomers(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Customer> customerPage = customerRepository.findAll(pageable);

    return customerPage.map(this::convertToResponseDTO);
}
@Override
public List<CustomerResponseDTO> getAllCustomers(Sort sort) {
    List<Customer> list = customerRepository.findAll(sort);
    return list.stream().map(this::convertToResponseDTO).toList();
}
@Override
public CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO) {
    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    if (updateDTO.getFullName() != null)
        customer.setFullName(updateDTO.getFullName());

    if (updateDTO.getEmail() != null)
        customer.setEmail(updateDTO.getEmail());

    if (updateDTO.getPhone() != null)
        customer.setPhone(updateDTO.getPhone());

    if (updateDTO.getAddress() != null)
        customer.setAddress(updateDTO.getAddress());

    Customer updated = customerRepository.save(customer);
    return convertToResponseDTO(updated);
}

}
