package com.portfolio.banktransfercore.infrastructure.config;

import com.portfolio.banktransfercore.application.ports.in.TransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.services.ProcessTransferService;
import com.portfolio.banktransfercore.domain.Account;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class ApplicationConfig {

    @Bean
    public TransferUseCase transferUseCase() {
        AccountRepositoryPort mockRepository = new AccountRepositoryPort() {
            @Override
            public Optional<Account> findByAccountNumber(String number) {
                return Optional.empty();
            }

            @Override
            public Optional<Account> findById(UUID id) {
                return Optional.empty();
            }

            @Override
            public void save(Account account) {
            }
        };

        return new ProcessTransferService(mockRepository);
    }
}