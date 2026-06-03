package com.portfolio.banktransfercore.infrastructure.config;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import com.portfolio.banktransfercore.application.ports.out.AccountRepositoryPort;
import com.portfolio.banktransfercore.application.services.FundsTransferService;
import com.portfolio.banktransfercore.infrastructure.transactional.TransactionalFundsTransferDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

  @Bean
  public FundsTransferUseCase transferUseCase(AccountRepositoryPort accountRepositoryPort) {
    var service = new FundsTransferService(accountRepositoryPort);
    return new TransactionalFundsTransferDecorator(service);
  }
}
