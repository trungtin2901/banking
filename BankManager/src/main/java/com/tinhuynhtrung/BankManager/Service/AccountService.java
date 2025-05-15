package com.tinhuynhtrung.BankManager.Service;

import com.tinhuynhtrung.BankManager.Config.AccountBalanceConfig;
import com.tinhuynhtrung.BankManager.DTO.Request.AccountRequestDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.AccountResponseDTO;
import com.tinhuynhtrung.BankManager.Entity.Account;
import com.tinhuynhtrung.BankManager.Entity.Customer;
import com.tinhuynhtrung.BankManager.Exception.ResourceNotFoundException;
import com.tinhuynhtrung.BankManager.Repository.AccountRepository;
import com.tinhuynhtrung.BankManager.Repository.CustomerRepository;
import com.tinhuynhtrung.BankManager.Repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final AccountBalanceConfig accountBalanceConfig;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          CustomerRepository customerRepository,
                          TransactionRepository transactionRepository,
                          ModelMapper modelMapper,
                          AccountBalanceConfig accountBalanceConfig) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.modelMapper = modelMapper;
        this.accountBalanceConfig = accountBalanceConfig;
    }

    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        Customer customer = customerRepository.findById(accountRequestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + accountRequestDTO.getCustomerId()));

        Account account = modelMapper.map(accountRequestDTO, Account.class);
        account.setCustomer(customer);
        account.setOpeningDate(LocalDate.now());

        Account savedAccount = accountRepository.save(account);
        return convertToResponseDTO(savedAccount);
    }

    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return convertToResponseDTO(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponseDTO> getActiveAccountsByCustomerId(Long customerId) {
        return accountRepository.findActiveByCustomerId(customerId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteAccount(Long id) {
        Account account = accountRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active account not found with id: " + id));

        // Soft delete all account's transactions first
        account.getTransactions().forEach(transaction ->
                transactionRepository.softDelete(transaction.getId(), LocalDateTime.now()));

        // Then soft delete the account
        accountRepository.softDelete(id, LocalDateTime.now());
    }

    public String getAccountBalanceLevel(BigDecimal balance) {
        if (balance.compareTo(accountBalanceConfig.getHigh()) >= 0) {
            return "HIGH";
        } else if (balance.compareTo(accountBalanceConfig.getMedium()) >= 0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    private AccountResponseDTO convertToResponseDTO(Account account) {
        AccountResponseDTO responseDTO = modelMapper.map(account, AccountResponseDTO.class);
        responseDTO.setCustomerId(account.getCustomer().getId());
        responseDTO.setCustomerName(account.getCustomer().getName());
        return responseDTO;
    }
}
