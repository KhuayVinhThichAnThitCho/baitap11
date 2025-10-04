package com.example.bai11.controller;

import com.example.bai11.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final List<Customer> customers = List.of(
        Customer.builder().id("001").name("Dương Thế Vinh")
                .phoneNumber("0978621161").email("dtvinh.a1.20.21@gmail.com").build(),
        Customer.builder().id("002").name("Nguyễn Hữu Trung")
                .phoneNumber("0900000002").email("trunghuu@gmail.com").build()
    );
    
    // Public (ai cũng vào được)
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello is Guest");
    }

    // Chỉ ADMIN
    @GetMapping("/customer/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Customer>> getCustomerList() {
        return ResponseEntity.ok(customers);
    }

    // Chỉ USER
    @GetMapping("/customer/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
        return ResponseEntity.ok(
            customers.stream()
                     .filter(c -> c.getId().equals(id))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Not found: " + id))
        );
    }
}
