package com.portfolio.banktransfercore.infrastructure.config;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.services.FundsTransferService;
import com.portfolio.banktransfercore.domain.account.Account;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public FundsTransferUseCase transferUseCase() {
    AccountRepositoryPort mockRepository =
        new AccountRepositoryPort() {
          @Override
          public Optional<Account> findByAccountNumber(String number) {
            return Optional.empty();
          }

          @Override
          public Optional<Account> findById(UUID id) {
            return Optional.empty();
          }

          @Override
          public void save(Account account) {}
        };

    return new FundsTransferService(mockRepository);
  }
}
