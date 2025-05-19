package com.tinhuynhtrung.BankManager.Controller;

import com.tinhuynhtrung.BankManager.DTO.Request.CustomerRequestDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.CustomerResponseDTO;
import com.tinhuynhtrung.BankManager.Service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createCustomer(
            @Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        CustomerResponseDTO customerResponseDTO = customerService.createCustomer(customerRequestDTO);
        return new ResponseEntity<>(customerResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllActiveCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id){
        CustomerResponseDTO customer = customerService.getActiveCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO customerRequestDTO
    ){
        CustomerResponseDTO updateCustomer = customerService.updateCustomer(id, customerRequestDTO);
        return ResponseEntity.ok(updateCustomer);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerResponseDTO>> searchCustomers(
            @RequestParam String keyword){

        List<CustomerResponseDTO> results = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void softDeleteCustomer(@PathVariable Long id) {
        customerService.softDeleteCustomer(id);
    }
}
