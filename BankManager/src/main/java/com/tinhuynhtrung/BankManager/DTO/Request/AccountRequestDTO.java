package com.tinhuynhtrung.BankManager.DTO.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequestDTO {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", message = "Balance must be positive")
    private BigDecimal balance;

    @NotNull(message = "Transaction limit is required")
    @DecimalMin(value = "0.0", message = "Transaction limit must be positive")
    private BigDecimal transactionLimit;

    @NotNull(message = "Customer ID is required")
    private Long customerId;
}
