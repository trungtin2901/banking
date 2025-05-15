package com.tinhuynhtrung.BankManager.Config;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "transaction.fee")
public class AccountBalanceConfig {
    @NotNull
    private BigDecimal medium;

    @NotNull
    private BigDecimal high;


}
