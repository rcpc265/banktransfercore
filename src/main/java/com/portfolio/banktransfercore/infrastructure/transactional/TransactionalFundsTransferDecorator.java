package com.portfolio.banktransfercore.infrastructure.transactional;

import com.portfolio.banktransfercore.application.ports.in.FundsTransferUseCase;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionalFundsTransferDecorator implements FundsTransferUseCase {

  private final FundsTransferUseCase delegate;

  public TransactionalFundsTransferDecorator(FundsTransferUseCase delegate) {
    this.delegate = delegate;
  }

  @Override
  @Transactional
  public void execute(
      String sourceNumber, String destinationNumber, BigDecimal amount, String currency) {
    delegate.execute(sourceNumber, destinationNumber, amount, currency);
  }
}
