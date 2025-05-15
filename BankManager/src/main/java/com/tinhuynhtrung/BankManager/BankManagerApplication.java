package com.tinhuynhtrung.BankManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BankManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankManagerApplication.class, args);
	}

}
