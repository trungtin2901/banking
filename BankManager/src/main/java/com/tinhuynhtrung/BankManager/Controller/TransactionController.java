package com.tinhuynhtrung.BankManager.Controller;

import com.tinhuynhtrung.BankManager.DTO.Request.TransactionRequestDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.TransactionResponseDTO;
import com.tinhuynhtrung.BankManager.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestBody TransactionRequestDTO transactionRequestDTO) {
        TransactionResponseDTO responseDTO = transactionService.createTransaction(transactionRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByAccountId(
            @PathVariable Long accountId) {
        List<TransactionResponseDTO> transactions = transactionService.getActiveTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
