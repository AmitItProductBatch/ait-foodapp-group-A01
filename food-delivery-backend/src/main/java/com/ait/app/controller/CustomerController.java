package com.ait.app.controller;

import com.ait.app.model.Customer;
import com.ait.app.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * GET /api/users
     * Retrieve all customers/users.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllUsers() {
        List<Customer> users = customerRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * POST /api/users
     * Add a new customer/user.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty() ||
            customer.getEmail() == null || customer.getEmail().trim().isEmpty() ||
            customer.getAddress() == null || customer.getAddress().trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Name, Email, and Address are all required fields.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        try {
            Customer savedCustomer = customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to save user: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/users/health
     * API health status check.
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "food-delivery-backend");
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(status);
    }
}
