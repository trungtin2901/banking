package com.tinhuynhtrung.BankManager.DTO.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private String type;
    private BigDecimal fee;
    private String location;
    private LocalDateTime timestamp;
    private Long accountId;
}
