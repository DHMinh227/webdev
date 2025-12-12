package com.example.customerapi.controller;

import com.example.customerapi.dto.CustomerRequestDTO;
import com.example.customerapi.dto.CustomerResponseDTO;
import com.example.customerapi.service.CustomerService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerRestController {

    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> list = customerService.getAllCustomers();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO created = customerService.createCustomer(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO updated = customerService.updateCustomer(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().body(
                java.util.Collections.singletonMap("message", "Customer deleted successfully")
        );
    }
    @GetMapping("/search")
public ResponseEntity<List<CustomerResponseDTO>> searchCustomers(
        @RequestParam String keyword) {
    List<CustomerResponseDTO> customers = customerService.searchCustomers(keyword);
    return ResponseEntity.ok(customers);
}
@GetMapping("/status/{status}")
public ResponseEntity<List<CustomerResponseDTO>> getCustomersByStatus(
        @PathVariable String status) {
    List<CustomerResponseDTO> customers = customerService.getCustomersByStatus(status);
    return ResponseEntity.ok(customers);
}
@GetMapping("/advanced-search")
public ResponseEntity<List<CustomerResponseDTO>> advancedSearch(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String status) {

    List<CustomerResponseDTO> customers =
            customerService.advancedSearch(name, email, status);

    return ResponseEntity.ok(customers);
}
@GetMapping
public ResponseEntity<Map<String, Object>> getAllCustomers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    Page<CustomerResponseDTO> customerPage = customerService.getAllCustomers(page, size);

    Map<String, Object> response = new HashMap<>();
    response.put("customers", customerPage.getContent());
    response.put("currentPage", customerPage.getNumber());
    response.put("totalItems", customerPage.getTotalElements());
    response.put("totalPages", customerPage.getTotalPages());

    return ResponseEntity.ok(response);
}
@GetMapping("/sorted")
public ResponseEntity<List<CustomerResponseDTO>> getCustomersSorted(
        @RequestParam(required = false) String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    List<CustomerResponseDTO> customers = customerService.getAllCustomers(sort);
    return ResponseEntity.ok(customers);
}
@GetMapping("/page-sort")
public ResponseEntity<Map<String, Object>> getCustomersPageSort(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc")
            ? Sort.by(sortBy).ascending()
            : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<Customer> customerPage = customerRepository.findAll(pageable);

    Page<CustomerResponseDTO> dtoPage = customerPage.map(this::convertToResponseDTO);

    Map<String, Object> response = new HashMap<>();
    response.put("customers", dtoPage.getContent());
    response.put("currentPage", dtoPage.getNumber());
    response.put("totalItems", dtoPage.getTotalElements());
    response.put("totalPages", dtoPage.getTotalPages());

    return ResponseEntity.ok(response);
}
@PatchMapping("/{id}")
public ResponseEntity<CustomerResponseDTO> partialUpdateCustomer(
        @PathVariable Long id,
        @RequestBody CustomerUpdateDTO updateDTO) {

    CustomerResponseDTO updated = customerService.partialUpdateCustomer(id, updateDTO);
    return ResponseEntity.ok(updated);
}

}
