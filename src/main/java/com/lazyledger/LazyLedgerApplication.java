package com.lazyledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.lazyledger", "com.lazyledger.transaction.infrastructure.externalApps"})
public class LazyLedgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyLedgerApplication.class, args);

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}