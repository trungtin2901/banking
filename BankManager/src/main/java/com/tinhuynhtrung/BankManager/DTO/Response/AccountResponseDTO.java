package com.tinhuynhtrung.BankManager.DTO.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private BigDecimal transactionLimit;
    private LocalDate openDate;
    private Long customerId;
    private String customerName;
}
