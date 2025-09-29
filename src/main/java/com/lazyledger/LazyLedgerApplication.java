package com.lazyledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.lazyledger"})
@EnableJpaRepositories(basePackages = {"com.lazyledger.ledger.infrastructure.persistance.postgres"})
public class LazyLedgerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LazyLedgerApplication.class, args);

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}