package com.tinhuynhtrung.BankManager.Config;

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
public class TransactionFeeConfig {
    private BigDecimal percentage;

    public BigDecimal getFeePercentage() {
        return percentage;
    }


}
