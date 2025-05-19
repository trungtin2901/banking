package com.tinhuynhtrung.BankManager.Service;

import com.tinhuynhtrung.BankManager.DTO.Request.CustomerRequestDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.CustomerResponseDTO;
import com.tinhuynhtrung.BankManager.Entity.Customer;
import com.tinhuynhtrung.BankManager.Exception.ResourceNotFoundException;
import com.tinhuynhtrung.BankManager.Repository.AccountRepository;
import com.tinhuynhtrung.BankManager.Repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = modelMapper.map(customerRequestDTO, Customer.class);
        customer.setCreatedAt(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllActiveCustomers() {
        return customerRepository.findAllActive().stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDTO getActiveCustomerById(Long id) {
        Customer customer = customerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active customer not found with id: " + id));
        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

    @Transactional
    public void softDeleteCustomer(Long id) {
        Customer customer = customerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active customer not found with id: " + id));

        // Soft delete all customer's accounts first
        customer.getAccounts().forEach(account ->
                accountRepository.softDelete(account.getId(), LocalDateTime.now()));

        // Then soft delete the customer
        customerRepository.softDelete(id, LocalDateTime.now());
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO customerRequestDTO) {
        Customer existingCustomer = customerRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        existingCustomer.setModifiedAt(LocalDateTime.now());

        modelMapper.map(customerRequestDTO, existingCustomer);

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return modelMapper.map(updatedCustomer, CustomerResponseDTO.class);
    }

    public List<CustomerResponseDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchByName(keyword);

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDTO.class))
                .collect(Collectors.toList());
    }

//    public void deleteCustomer(Long id) {
//        if (!customerRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Customer not found with id: " + id);
//        }
//        customerRepository.deleteById(id);
//    }
}
