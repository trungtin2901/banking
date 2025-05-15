package com.tinhuynhtrung.BankManager.Controller;


import com.tinhuynhtrung.BankManager.DTO.Response.AccountResponseDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.CustomerResponseDTO;
import com.tinhuynhtrung.BankManager.Service.AccountService;
import com.tinhuynhtrung.BankManager.Service.CustomerService;
import com.tinhuynhtrung.BankManager.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    @Autowired
    public StatisticsController(AccountService accountService,
                                CustomerService customerService,
                                TransactionService transactionService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<Map<String, Long>> getAccountStatistics() {
        Map<String, Long> stats = new HashMap<>();
        List<AccountResponseDTO> accounts = accountService.getAllAccounts();

        stats.put("totalAccounts", (long) accounts.size());
        stats.put("highBalanceAccounts", accounts.stream()
                .filter(account -> "HIGH"
                        .equals(accountService.getAccountBalanceLevel(account.getBalance())))
                .count());
        stats.put("mediumBalanceAccounts", accounts.stream()
                .filter(account -> "MEDIUM"
                        .equals(accountService.getAccountBalanceLevel(account.getBalance())))
                .count());
        stats.put("lowBalanceAccounts", accounts.stream()
                .filter(account -> "LOW"
                        .equals(accountService.getAccountBalanceLevel(account.getBalance())))
                .count());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/customers/location")
    public ResponseEntity<Map<String, Long>> getCustomerLocationStatistics() {
        Map<String, Long> locationStats = customerService.getAllActiveCustomers().stream()
                .collect(Collectors.groupingBy(
                        CustomerResponseDTO::getAddress,
                        Collectors.counting()
                ));
        return ResponseEntity.ok(locationStats);
    }
}
