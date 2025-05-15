package com.tinhuynhtrung.BankManager.Service;

import com.tinhuynhtrung.BankManager.Config.TransactionFeeConfig;
import com.tinhuynhtrung.BankManager.DTO.Request.TransactionRequestDTO;
import com.tinhuynhtrung.BankManager.DTO.Response.TransactionResponseDTO;
import com.tinhuynhtrung.BankManager.Entity.Account;
import com.tinhuynhtrung.BankManager.Entity.Transaction;
import com.tinhuynhtrung.BankManager.Entity.TransactionType;
import com.tinhuynhtrung.BankManager.Exception.ResourceNotFoundException;
import com.tinhuynhtrung.BankManager.Exception.TransactionLimitExceededException;
import com.tinhuynhtrung.BankManager.Repository.AccountRepository;
import com.tinhuynhtrung.BankManager.Repository.TransactionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final TransactionFeeConfig transactionFeeConfig;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              ModelMapper modelMapper,
                              TransactionFeeConfig transactionFeeConfig) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.transactionFeeConfig = transactionFeeConfig;
    }

    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO transactionRequestDTO) {
        Account account = accountRepository.findById(transactionRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + transactionRequestDTO.getAccountId()));

        BigDecimal amount = transactionRequestDTO.getAmount();
        String type = transactionRequestDTO.getType().toUpperCase();

        // Calculate fee
        BigDecimal fee = amount.multiply(transactionFeeConfig.getFeePercentage());

        // Check transaction limit for withdrawals and transfers
        if (("WITHDRAWAL".equals(type) || "TRANSFER".equals(type)) &&
                amount.compareTo(account.getTransactionLimit()) > 0) {
            throw new TransactionLimitExceededException("Transaction amount exceeds the account limit");
        }

        // Update account balance
        BigDecimal newBalance;
        if ("DEPOSIT".equals(type)) {
            newBalance = account.getBalance().add(amount).subtract(fee);
        } else {
            newBalance = account.getBalance().subtract(amount).subtract(fee);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Insufficient funds for this transaction");
            }
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create and save transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setFee(fee);
        transaction.setLocation(transactionRequestDTO.getLocation());
        transaction.setTimestamp(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return convertToResponseDTO(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> getActiveTransactionsByAccountId(Long accountId) {
        return transactionRepository.findActiveByAccountId(accountId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void softDeleteTransaction(Long id) {
        if (!transactionRepository.findActiveById(id).isPresent()) {
            throw new ResourceNotFoundException("Active transaction not found with id: " + id);
        }
        transactionRepository.softDelete(id, LocalDateTime.now());
    }

    private TransactionResponseDTO convertToResponseDTO(Transaction transaction) {
        TransactionResponseDTO responseDTO = modelMapper.map(transaction, TransactionResponseDTO.class);
        responseDTO.setAccountId(transaction.getAccount().getId());
        return responseDTO;
    }
}
