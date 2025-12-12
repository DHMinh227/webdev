package com.example.customerapi.dto;

import jakarta.validation.constraints.*;

public class CustomerRequestDTO {

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^C\\d{3,}$")
    private String customerCode;

    @NotBlank
    @Size(min = 2, max = 100)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,20}$")
    private String phone;

    @Size(max = 500)
    private String address;

    @NotBlank
    private String status;

    public CustomerRequestDTO() {}

    public CustomerRequestDTO(String customerCode, String fullName, String email,
                              String phone, String address, String status) {
        this.customerCode = customerCode;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
