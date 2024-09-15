package com.khalil.customer.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service ;


    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok().body(service.createCustomer(request)) ;
    }

    @PutMapping
    public ResponseEntity<String> updateCustomer(@RequestBody @Valid CustomerRequest request  ) {
        service.updateCustomer(request) ;
        return ResponseEntity.ok().body("Customer updated") ;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok().body(service.findAllCustomers()) ;
    }

    @GetMapping("/exists/{customer_id}")
    public ResponseEntity<Boolean> existsById(@PathVariable String customer_id) {
        return ResponseEntity.ok().body(service.existsById(customer_id)) ;
    }

    @GetMapping("/{customer_id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable String customer_id) {
        return ResponseEntity.ok().body(service.findById(customer_id)) ;
    }

    @DeleteMapping("/{customer_id}")
    public ResponseEntity<CustomerResponse> deleteCustomer(@PathVariable String customer_id) {
        return ResponseEntity.accepted().body(service.deleteById(customer_id)) ;
    }
}
